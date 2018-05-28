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











