package backend.tracker.domain

import backend.common.types._
import backend.common.types.member._
import backend.common.types.organization.OrganizationId
import library.eventsourcing.{AggregateRoot, AggregateRootInfo}
import org.joda.time.DateTime

case class Member private(
    id: MemberId,
    name: MemberName,
    email: Email,
    role: MemberRole,
    organizationId: OrganizationId,
    becameMemberAt: DateTime,
    aggregateRootInfo: AggregateRootInfo[MemberDomainEvent]
) extends AggregateRoot[Member, MemberId, MemberDomainEvent]{

  override def applyEvent(event: MemberDomainEvent): Member = event match {
    case e: MemberCreated => Member(e.id, e.name, e.email, e.role, e.organizationId, e.becameMemberAt, aggregateRootInfo)
    case e: MemberNameChanged => copy(name = e.name)
    case e: MemberEmailChanged => copy(email = e.email)
    case e: MemberBecameAnOwner => copy(role = e.role)
    case e: MemberUnBecameAnOwner => this // do nothing
    case e: MemberBecameAStandardMember => copy(role = e.role)
    case e: MemberDisconnected => copy(role = FormerMember)
  }

  override def idAsLong: Long = id.value

  override def copyWithInfo(info: AggregateRootInfo[MemberDomainEvent]): Member = copy(aggregateRootInfo = info)
}

object Member {
  def apply(id: MemberId, name: MemberName, email: Email, role: MemberRole, organizationId: OrganizationId, becameMemberAt: DateTime): Member = {
    val events = List(MemberCreated(id, name, email, role, organizationId, becameMemberAt))
    Member(id, name, email, role,  organizationId, becameMemberAt, AggregateRootInfo(events, 0))
  }

  val empty: Member = Member(MemberId(0), MemberName("", validate = false), Email("", validate = false), StandardMember, OrganizationId(0), DateTime.now, AggregateRootInfo(Nil, 0))
}
