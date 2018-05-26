package backend.tracker.domain

import backend.common.types._
import backend.membership.domain.MemberDomainEvent
import library.eventsourcing.{AggregateRoot, AggregateRootInfo}
import org.joda.time.DateTime

case class Member(
    id: MemberId,
    name: MemberName,
    email: Email,
    Role: MemberRole,
    organizationId: OrganizationId,
    becameMemberAt: DateTime,
    aggregateRootInfo: AggregateRootInfo[MemberDomainEvent]
) extends AggregateRoot[Member, MemberId, MemberDomainEvent]{

  override def applyEvent(event: MemberDomainEvent): Member = ???

  override def idAsLong: Long = id.value

  override def copyWithInfo(info: AggregateRootInfo[MemberDomainEvent]): Member = copy(aggregateRootInfo = info)
}

object Member {
  def create(id: MemberId, name: MemberName, email: Email, role: MemberRole, organizationId: OrganizationId, becameMemberAt: DateTime): Member = {
    val events = List(MemberCreated(id, name, email, role, organizationId, becameMemberAt))
    Member(id, name, email, role,  organizationId, becameMemberAt, AggregateRootInfo(events, 0))
  }

  val empty: Member = Member(MemberId(0), MemberName("", validate = false), Email("", validate = false), StandardMember, OrganizationId(0), DateTime.now, AggregateRootInfo(Nil, 0))
}
