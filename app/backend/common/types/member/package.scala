package backend.common.types

import library.error.ValidationException
import library.validation.{DefaultMessage, StringValidatable}
import play.api.libs.json.{JsNumber, JsString, Reads, Writes}

package object member {

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

  sealed trait MemberRole

  final case object Owner extends MemberRole
  final case object StandardMember extends MemberRole
  final
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
}
