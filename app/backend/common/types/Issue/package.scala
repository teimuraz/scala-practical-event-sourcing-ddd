package backend.common.types

import library.error.ValidationException
import library.validation
import library.validation.{DefaultMessage, StringValidatable}
import play.api.libs.json.{JsNumber, JsString, Reads, Writes}

package object Issue {

  case class IssueId private(value: Long) extends AnyVal

  object IssueId {
    implicit val reads: Reads[IssueId] = Reads.of[Long].map(IssueId(_))
    implicit val writes: Writes[IssueId] = (o: IssueId) => JsNumber(o.value)
  }

  case class IssueTitle private(value: String) extends AnyVal

  object IssueTitle extends StringValidatable[IssueTitle] {
    override def notEmpty = Some(DefaultMessage)
    override def inst = new IssueTitle(_)
    implicit val reads: Reads[IssueTitle] = Reads.of[String].map(IssueTitle(_))
    implicit val writes: Writes[IssueTitle] = (o: IssueTitle) => JsString(o.value)
  }

  case class IssueDescription private(value: String) extends AnyVal

  object IssueDescription extends StringValidatable[IssueDescription] {
    override def inst = new IssueDescription(_)
    override def maxLength: Option[(Int, validation.ValidationMessage)] = Some((500, DefaultMessage))
    implicit val reads: Reads[IssueDescription] = Reads.of[String].map(IssueDescription(_))
    implicit val writes: Writes[IssueDescription] = (o: IssueDescription) => JsString(o.value)
  }

  sealed trait IssueStatus
  final case object Open extends IssueStatus
  final case object Closed extends IssueStatus

  object IssueStatus {
    def valueOf(value: Int): IssueStatus = {
      value match {
        case 1 => Open
        case 100 => Closed
        case _ => throw new ValidationException(s"Not supported issue status: $value")
      }
    }

    def intValueOf(role: IssueStatus): Int = role match {
      case Open => 1
      case Closed => 100
    }

    implicit val reads: Reads[IssueStatus] = Reads.of[Int].map(valueOf)
    implicit val writes: Writes[IssueStatus] = (o: IssueStatus) => JsNumber(intValueOf(o))
  }
}
