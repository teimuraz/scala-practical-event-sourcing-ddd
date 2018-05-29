package backend.tracker.api.event

import backend.common.types._
import backend.common.types.member.{MemberId, MemberName, MemberRole}
import backend.common.types.organization.OrganizationId
import julienrf.json.derived.flat
import library.eventsourcing.AggregateRootEvent
import library.joda.json.jsonDateTimeFormat
import org.joda.time.DateTime
import play.api.libs.json._

sealed trait MemberEvent extends AggregateRootEvent {
  def id: MemberId
}

case class MemberCreated(
  id: MemberId,
  name: MemberName,
  email: Email,
  role: MemberRole,
  organizationId: OrganizationId,
  becameMemberAt: DateTime
) extends MemberEvent

object MemberCreated {
  implicit val format: OFormat[MemberCreated] = Json.format[MemberCreated]
}

case class MemberNameChanged(id: MemberId, name: MemberName) extends MemberEvent

object MemberNameChanged {
  implicit val format: OFormat[MemberNameChanged] = Json.format[MemberNameChanged]
}

case class MemberEmailChanged(id: MemberId, email: Email) extends MemberEvent

object MemberEmailChanged {
  implicit val format: OFormat[MemberEmailChanged] = Json.format[MemberEmailChanged]
}

case class MemberBecameAnOwner(id: MemberId, role: MemberRole) extends MemberEvent

object MemberBecameAnOwner {
  implicit val format: OFormat[MemberBecameAnOwner] = Json.format[MemberBecameAnOwner]
}

case class MemberBecameAStandardMember(id: MemberId, role: MemberRole) extends MemberEvent

object MemberBecameAStandardMember {
  implicit val format: OFormat[MemberBecameAStandardMember] = Json.format[MemberBecameAStandardMember]
}

case class MemberUnBecameAnOwner(id: MemberId, role: MemberRole) extends MemberEvent

object MemberUnBecameAnOwner {
  implicit val format: OFormat[MemberUnBecameAnOwner] = Json.format[MemberUnBecameAnOwner]
}

case class MemberDisconnected(id: MemberId, role: MemberRole) extends MemberEvent

object MemberDisconnected {
  implicit val format: OFormat[MemberDisconnected] = Json.format[MemberDisconnected]
}

object MemberEvent {
  implicit lazy val format: OFormat[MemberEvent] = flat.oformat((__ \ "eventType").format[String])
}

