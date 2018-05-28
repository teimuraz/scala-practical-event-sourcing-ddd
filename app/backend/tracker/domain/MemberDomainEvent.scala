package backend.tracker.domain

import backend.common.types._
import backend.common.types.member.{MemberId, MemberName, MemberRole}
import julienrf.json.derived.flat
import library.eventsourcing.AggregateRootEvent
import org.joda.time.DateTime
import play.api.libs.json._
import library.joda.json.jsonDateTimeFormat

sealed trait MemberDomainEvent extends AggregateRootEvent {
  def id: MemberId
}

case class MemberCreated(
  id: MemberId,
  name: MemberName,
  email: Email,
  role: MemberRole,
  organizationId: OrganizationId,
  becameMemberAt: DateTime
) extends MemberDomainEvent

object MemberCreated {
  implicit val format: OFormat[MemberCreated] = Json.format[MemberCreated]
}

case class MemberNameChanged(id: MemberId, name: MemberName) extends MemberDomainEvent

object MemberNameChanged {
  implicit val format: OFormat[MemberNameChanged] = Json.format[MemberNameChanged]
}

case class MemberEmailChanged(id: MemberId, email: Email) extends MemberDomainEvent

object MemberEmailChanged {
  implicit val format: OFormat[MemberEmailChanged] = Json.format[MemberEmailChanged]
}

case class MemberBecameAnOwner(id: MemberId, role: MemberRole) extends MemberDomainEvent

object MemberBecameAnOwner {
  implicit val format: OFormat[MemberBecameAnOwner] = Json.format[MemberBecameAnOwner]
}

case class MemberBecameAStandardMember(id: MemberId, role: MemberRole) extends MemberDomainEvent

object MemberBecameAStandardMember {
  implicit val format: OFormat[MemberBecameAStandardMember] = Json.format[MemberBecameAStandardMember]
}

case class MemberUnBecameAnOwner(id: MemberId, role: MemberRole) extends MemberDomainEvent

object MemberUnBecameAnOwner {
  implicit val format: OFormat[MemberUnBecameAnOwner] = Json.format[MemberUnBecameAnOwner]
}

case class MemberDisconnected(id: MemberId, role: MemberRole) extends MemberDomainEvent

object MemberDisconnected {
  implicit val format: OFormat[MemberDisconnected] = Json.format[MemberDisconnected]
}

object MemberDomainEvent {
  implicit lazy val format: OFormat[MemberDomainEvent] = flat.oformat((__ \ "eventType").format[String])
}

