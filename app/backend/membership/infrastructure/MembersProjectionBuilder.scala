package backend.membership.infrastructure

import backend.common.{MemberRole, Owner}
import javax.inject.{Inject, Singleton}
import library.jooq.{Db, JooqRepositorySupport}
import library.messaging.{Subscriber, Topic}
import library.repository.RepComponents
import backend.jooq.generated.Tables.MEMBERSHIP_MEMBERS
import backend.membership.domain._

@Singleton
class MembersProjectionBuilder @Inject()
    (memberTopic: MemberDomainEventTopic)
  extends Subscriber[MemberDomainEvent, RepComponents]
  with JooqRepositorySupport {

  override def topic: Topic[MemberDomainEvent, RepComponents] = memberTopic

  override def handle(message: MemberDomainEvent)(implicit additionalData: RepComponents): Unit = {
    message match {
      case e: MemberCreated => handle(e)
      case e: MemberNameChanged => handle(e)
      case e: MemberEmailChanged => handle(e)
      case e: MemberBecameAnOwner => handle(e)
      case e: MemberBecameAStandardMember => handle(e)
      case e: MemberDisconnected => handle(e)
      case _ => ()
    }
  }

  private def handle(e: MemberCreated)(implicit rc: RepComponents) = {
    rc.dsl
      .insertInto(
        MEMBERSHIP_MEMBERS,
        MEMBERSHIP_MEMBERS.ID,
        MEMBERSHIP_MEMBERS.NAME,
        MEMBERSHIP_MEMBERS.EMAIL,
        MEMBERSHIP_MEMBERS.ROLE,
        MEMBERSHIP_MEMBERS.ORGANIZATION_ID,
        MEMBERSHIP_MEMBERS.BECAME_MEMBER_AT
      )
      .values(
        e.id.value,
        e.name.value,
        e.email.value,
        MemberRole.intValueOf(e.role),
        e.organizationId.value,
        e.becameMemberAt
      )
      .execute()
  }

  private def handle(e: MemberNameChanged)(implicit rc: RepComponents) = {
    rc.dsl
      .update(MEMBERSHIP_MEMBERS)
      .set(MEMBERSHIP_MEMBERS.NAME, e.name.value)
      .where(MEMBERSHIP_MEMBERS.ID.eq(e.id.value))
      .execute()
  }

  private def handle(e: MemberEmailChanged)(implicit rc: RepComponents) = {
    rc.dsl
      .update(MEMBERSHIP_MEMBERS)
      .set(MEMBERSHIP_MEMBERS.EMAIL, e.email.value)
      .where(MEMBERSHIP_MEMBERS.ID.eq(e.id.value))
      .execute()
  }

  private def handle(e: MemberBecameAnOwner)(implicit rc: RepComponents) = {
    updateMemberRole(e.id, e.role)
  }

  private def handle(e: MemberBecameAStandardMember)(implicit rc: RepComponents) = {
    updateMemberRole(e.id, e.role)
  }

  private def handle(e: MemberDisconnected)(implicit rc: RepComponents) = {
    updateMemberRole(e.id, e.role)
    rc.dsl
      .deleteFrom(MEMBERSHIP_MEMBERS)
      .where(MEMBERSHIP_MEMBERS.ID.eq(e.id.value))
      .execute()
  }

  private def updateMemberRole(memberId: MemberId, role: MemberRole)(implicit rc: RepComponents) = {
    rc.dsl
      .update(MEMBERSHIP_MEMBERS)
      .set(MEMBERSHIP_MEMBERS.ROLE, new Integer(MemberRole.intValueOf(role)))
      .where(MEMBERSHIP_MEMBERS.ID.eq(memberId.value))
      .execute()
  }
}
