package backend.common.types

import library.validation.{DefaultMessage, StringValidatable}
import play.api.libs.json.{JsNumber, JsString, Reads, Writes}

package object organization {

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

}
