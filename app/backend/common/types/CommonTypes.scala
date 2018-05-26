package backend.common.types

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

sealed trait MemberRole
case object Owner extends MemberRole
case object StandardMember extends MemberRole
case object FormerMember extends MemberRole

object MemberRole {
  def valueOf(value: Int): MemberRole = {
    value match {
      case 1 => StandardMember
      case 99 => FormerMember
      case 100 => Owner
      case _ => throw new ValidationException(s"Not supported member role: $value")
    }
  }

  def intValueOf(role: MemberRole): Int = role match {
    case StandardMember => 1
    case FormerMember => 99
    case Owner => 100
  }

  implicit val reads: Reads[MemberRole] = Reads.of[Int].map(valueOf)
  implicit val writes: Writes[MemberRole] = (o: MemberRole) => JsNumber(intValueOf(o))
}


case class MemberId private(value: Long) extends AnyVal

object MemberId {
  implicit val reads: Reads[MemberId] = Reads.of[Long].map(MemberId(_))
  implicit val writes: Writes[MemberId] = (o: MemberId) => JsNumber(o.value)
}

case class MemberName private(value: String) extends AnyVal

object MemberName extends StringValidatable[MemberName] {
  override def notEmpty = Some(DefaultMessage)
  override def inst = new MemberName(_)
  implicit val reads: Reads[MemberName] = Reads.of[String].map(MemberName(_))
  implicit val writes: Writes[MemberName] = (o: MemberName) => JsString(o.value)
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