package library

import library.error.ValidationException
import library.util._

package object validation {

  def must(requirement: Boolean, message: String = ""): Unit = {
    if (!requirement) {
      val msg = if (message.isEmpty) "Requirement failed" else message
      throw new ValidationException(message)
    }
  }

  type Validation[V] = V => (Boolean, String)

  trait Validatable[V, T] {

    def validations: Seq[Validation[V]]

    def apply(value: V, validate: Boolean): T = {
      val normalized = normalize(value)

      if (validate) {
        validations.foreach { validation =>
          val (result, msg) = validation(normalized)
          must(result, msg)
        }
      }
      inst(normalized)
    }

    def apply(value: V): T = {
      apply(value, true)
    }

    def inst: V => T

    def normalize(v: V): V = v
  }

  sealed trait ValidationMessage
  final case object DefaultMessage extends ValidationMessage
  final case class Custom(msg: String) extends ValidationMessage

  trait StringValidatable[T] extends Validatable[String, T] {
    def notEmpty: Option[ValidationMessage] = None
    def notEmptyProp: Option[ValidationMessage] = None
    def minLength: Option[(Int, ValidationMessage)] = None
    def maxLength: Option[(Int, ValidationMessage)] = None

    /**
     * Trim string before validation.
     * Value will be constructed with full trimmed value.
     * @return
     */
    def trim: Boolean = true

    /**
     * Full trim string (trim string and also remove extra white space characters from the middle string).
     * Value will be constructed with full trimmed value.
     * #trim overwrites this value, e.g. if #trim == False and #fullTrim == True, then #fullTrim = False
     * @return
     */
    def fullTrim: Boolean = true

    def notEmptyValidation: Option[String => (Boolean, String)] = notEmpty.map { msg =>
      val message = msg match {
        case DefaultMessage => s"${this.getClass.getSimpleName.replace("$", "")} must not be empty"
        case Custom(m) => m
      }
      v: String => (!v.isEmpty, message)
    }

    def minLengthValidation: Option[String => (Boolean, String)] = minLength.map { case (lnt, msg) =>
      val message = msg match {
        case DefaultMessage => s"${this.getClass.getSimpleName.replace("$", "")} must contain at lest $lnt characters"
        case Custom(m) => m
      }

      v: String => (v.length >= lnt, message)
    }

    def maxLengthValidation: Option[String => (Boolean, String)] = maxLength.map { case (lnt, msg) =>
      val message = msg match {
        case DefaultMessage => s"${this.getClass.getSimpleName.replace("$", "")} must contain at most $lnt characters"
        case Custom(m) => m
      }
      v: String => (v.length <= lnt, message)
    }

    override def validations: Seq[Validation[String]] = {
      Seq(
        notEmptyValidation,
        minLengthValidation,
        maxLengthValidation
      )
      .flatten
    }

    override def normalize(v: String): String = {
      val makeFullTrim = if (!trim) false else fullTrim
      if (makeFullTrim) {
        v.fullTrim
      } else if (trim) {
        v.trim
      } else {
        v
      }
    }
  }

  trait EmailValidatable[T] extends StringValidatable[T] {
    def validEmail: ValidationMessage = DefaultMessage
    override def notEmpty: Option[ValidationMessage] = Some(DefaultMessage)
    override def fullTrim: Boolean = false
    private val emailRegex = """^[a-zA-Z0-9\.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$""".r

    def correctEmail: String => (Boolean, String) = {
      val message = validEmail match {
        case DefaultMessage => s"${this.getClass.getSimpleName.replace("$", "")} must be correct email address"
        case Custom(m) => m
      }
      v: String => (emailRegex.findFirstMatchIn(v).isDefined, message)
    }

    override def validations: Seq[Validation[String]] = super.validations :+ correctEmail
  }
}
