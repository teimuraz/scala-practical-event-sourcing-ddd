package backend.common

import library.validation.{DefaultMessage, EmailValidatable, StringValidatable}
import play.api.libs.json.Reads.email
import play.api.libs.json._

case class Email private(value: String) extends AnyVal

object Email extends EmailValidatable[Email] {
  override def inst = new Email(_)
  implicit val reads: Reads[Email] = Reads.of[String].map(v => Email(v.trim))
  implicit val writes: Writes[Email] = (o: Email) => JsString(o.value)
}

case class FirstName private(value: String) extends AnyVal

object FirstName extends StringValidatable[FirstName] {
  override def notEmpty = Some(DefaultMessage)
  override def inst = new FirstName(_)
  implicit val reads: Reads[FirstName] = Reads.of[String].map(v => FirstName(v.trim))
  implicit val writes: Writes[FirstName] = (o: FirstName) => JsString(o.value)
}

case class LastName private(value: String) extends AnyVal

object LastName extends StringValidatable[LastName] {
  override def notEmpty = Some(DefaultMessage)
  override def inst = new LastName(_)
  implicit val reads: Reads[LastName] = Reads.of[String].map(v => LastName(v.trim))
  implicit val writes: Writes[LastName] = (o: LastName) => JsString(o.value)
}

case class Ip(value: String) extends AnyVal

object Ip {
  implicit val reads: Reads[Ip] = Reads.of[String].map(v => Ip(v.trim))
  implicit val writes: Writes[Ip] = (o: Ip) => JsString(o.value)
}

case class Money(amount: BigDecimal, currency: String)

object Money {
  implicit val format: OFormat[Money] = Json.format[Money]
}



case class UserId(value: Long) extends AnyVal

object UserId {
  implicit val reads: Reads[UserId] = Reads.of[Long].map(UserId(_))
  implicit val writes: Writes[UserId] = (o: UserId) => JsNumber(o.value)
}

case class Username private(value: String) extends AnyVal

object Username extends StringValidatable[Username] {
  override def notEmpty = Some(DefaultMessage)
  override def inst = new Username(_)
  implicit val reads: Reads[Username] = Reads.of[String].map(Username(_))
  implicit val writes: Writes[Username] = (o: Username) => JsString(o.value)
}

case class Password private(value: String) extends AnyVal

object Password extends StringValidatable[Password] {
  override def notEmpty = Some(DefaultMessage)
  override def inst = new Password(_)
  implicit val reads: Reads[Password] = Reads.of[String].map(v => Password(v.trim))
  implicit val writes: Writes[Password] = (o: Password) => JsString(o.value)
}