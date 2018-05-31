package backend.tracker.infrastructure

import backend.common.types.member.{MemberId, MemberRole}
import backend.jooq.generated.Tables.TRACKER_MEMBERS
import backend.tracker.api.event._
import javax.inject.{Inject, Singleton}
import library.jooq.JooqRepositorySupport
import library.messaging.{Subscriber, Topic}
import library.repository.RepComponents

@Singleton
class MembersProjectionBuilder @Inject()
    (val topic: Topic[MemberEvent, RepComponents])
  extends Subscriber[MemberEvent, RepComponents]
  with JooqRepositorySupport {

  override def handle(message: MemberEvent)(implicit additionalData: RepComponents): Unit = {
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
        TRACKER_MEMBERS,
        TRACKER_MEMBERS.ID,
        TRACKER_MEMBERS.NAME,
        TRACKER_MEMBERS.EMAIL,
        TRACKER_MEMBERS.ROLE,
        TRACKER_MEMBERS.ORGANIZATION_ID,
        TRACKER_MEMBERS.BECAME_MEMBER_AT
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
      .update(TRACKER_MEMBERS)
      .set(TRACKER_MEMBERS.NAME, e.name.value)
      .where(TRACKER_MEMBERS.ID.eq(e.id.value))
      .execute()
  }

  private def handle(e: MemberEmailChanged)(implicit rc: RepComponents) = {
    rc.dsl
      .update(TRACKER_MEMBERS)
      .set(TRACKER_MEMBERS.EMAIL, e.email.value)
      .where(TRACKER_MEMBERS.ID.eq(e.id.value))
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
      .deleteFrom(TRACKER_MEMBERS)
      .where(TRACKER_MEMBERS.ID.eq(e.id.value))
      .execute()
  }

  private def updateMemberRole(memberId: MemberId, role: MemberRole)(implicit rc: RepComponents) = {
    rc.dsl
      .update(TRACKER_MEMBERS)
      .set(TRACKER_MEMBERS.ROLE, new Integer(MemberRole.intValueOf(role)))
      .where(TRACKER_MEMBERS.ID.eq(memberId.value))
      .execute()
  }
}
