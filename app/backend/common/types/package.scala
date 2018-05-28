package backend.common
import library.validation.{DefaultMessage, EmailValidatable, StringValidatable}
import play.api.libs.json._


package object types {

  case class Email private(value: String) extends AnyVal

  object Email extends EmailValidatable[Email] {
    override def inst = new Email(_)
    implicit val reads: Reads[Email] = Reads.of[String].map(v => Email(v.trim))
    implicit val writes: Writes[Email] = (o: Email) => JsString(o.value)
  }
}
