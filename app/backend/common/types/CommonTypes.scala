package backend.common.types

import backend.common.types.member.MemberRole
import library.error.ValidationException
import library.validation.{DefaultMessage, EmailValidatable, StringValidatable}
import play.api.libs.json.Reads.email
import play.api.libs.json._

case class Email private(value: String) extends AnyVal

object Email extends EmailValidatable[Email] {
  override def inst = new Email(_)
  implicit val reads: Reads[Email] = Reads.of[String].map(v => Email(v.trim))
  implicit val writes: Writes[Email] = (o: Email) => JsString(o.value)
}





case class OrganizationId private(value: Long) extends AnyVal

object OrganizationId {
  implicit val reads: Reads[OrganizationId] = Reads.of[Long].map(OrganizationId(_))
  implicit val writes: Writes[OrganizationId] = (o: OrganizationId) => JsNumber(o.value)
}

case class OrganizationName private(value: String) extends AnyVal

object OrganizationName extends StringValidatable[OrganizationName] {
  override def notEmpty = Some(DefaultMessage)
  override def inst = new OrganizationName(_)
  implicit val reads: Reads[OrganizationName] = Reads.of[String].map(OrganizationName(_))
  implicit val writes: Writes[OrganizationName] = (o: OrganizationName) => JsString(o.value)
}