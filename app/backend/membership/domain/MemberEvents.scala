package backend.membership.domain

import backend.common.{Email, MemberRole}
import library.eventsourcing.DomainEvent
import org.joda.time.DateTime
import play.api.libs.json._
import library.joda.json.jsonDateTimeFormat

trait MemberDomainEvent extends DomainEvent

object MemberDomainEvent {
  implicit val reads: Reads[MemberDomainEvent] = {
    (__ \ "eventType").read[String].flatMap {
      case MemberCreated.eventType => implicitly[Reads[MemberCreated]].map(identity)
      case MemberNameChanged.eventType => implicitly[Reads[MemberNameChanged]].map(identity)
      case MemberEmailChanged.eventType => implicitly[Reads[MemberEmailChanged]].map(identity)
      case MemberBecameAnOwner.eventType => implicitly[Reads[MemberBecameAnOwner]].map(identity)
      case other => Reads(_ => JsError(s"Unknown event type $other"))
    }
  }
  implicit val writes: Writes[MemberDomainEvent] = Writes { event =>
    val (jsValue, eventType) = event match {
      case m: MemberCreated => (Json.toJson(m)(MemberCreated.format), MemberCreated.eventType)
      case m: MemberNameChanged => (Json.toJson(m)(MemberNameChanged.format), MemberNameChanged.eventType)
      case m: MemberEmailChanged => (Json.toJson(m)(MemberEmailChanged.format), MemberEmailChanged.eventType)
      case m: MemberBecameAnOwner => (Json.toJson(m)(MemberBecameAnOwner.format), MemberBecameAnOwner.eventType)
    }
    jsValue.transform(__.json.update((__ \ 'eventType).json.put(JsString(eventType)))).get
  }
}

case class MemberCreated(
    id: MemberId,
    name: MemberName,
    email: Email,
    role: MemberRole,
    organizationId: OrganizationId,
    becameMemberAt: DateTime
) extends MemberDomainEvent {
  override def eventType: String = MemberCreated.eventType
}

object MemberCreated {
  val eventType = "memberCreated"
  implicit val format: OFormat[MemberCreated] = Json.format[MemberCreated]
}

case class MemberNameChanged(id: MemberId, name: MemberName) extends MemberDomainEvent {
  override def eventType: String = MemberNameChanged.eventType
}

object MemberNameChanged {
  val eventType = "memberNameChanged"
  implicit val format: OFormat[MemberNameChanged] = Json.format[MemberNameChanged]
}

case class MemberEmailChanged(id: MemberId, email: Email) extends MemberDomainEvent {
  override def eventType: String = MemberEmailChanged.eventType
}

object MemberEmailChanged {
  val eventType = "memberEmailChanged"
  implicit val format: OFormat[MemberEmailChanged] = Json.format[MemberEmailChanged]
}

case class MemberBecameAnOwner(id: MemberId, role: MemberRole) extends MemberDomainEvent {
  override def eventType: String = MemberBecameAnOwner.eventType
}

object MemberBecameAnOwner {
  val eventType = "memberBecameAnOwner"
  implicit val format: OFormat[MemberBecameAnOwner] = Json.format[MemberBecameAnOwner]
}

case class MemberBecameAStandardMember(id: MemberId, role: MemberRole) extends MemberDomainEvent {
  override def eventType: String = MemberBecameAStandardMember.eventType
}

object MemberBecameAStandardMember {
  val eventType = "memberBecameAStandardMember"
  implicit val format: OFormat[MemberBecameAStandardMember] = Json.format[MemberBecameAStandardMember]
}
