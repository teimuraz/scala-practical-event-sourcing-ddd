package backend.membership.infrastructure

import backend.common.MemberRole
import backend.membership.api._
import javax.inject.{Inject, Singleton}
import library.jooq.{Db, JooqRepositorySupport}
import library.messaging.{Subscriber, Topic}
import library.repository.RepComponents
import backend.jooq.generated.Tables.MEMBERSHIP_MEMBERS

@Singleton
class MembersProjectionBuilder @Inject()
    (memberTopic: MemberTopic)
  extends Subscriber[MemberEvent, RepComponents]
  with JooqRepositorySupport {

  override def topic: Topic[MemberEvent, RepComponents] = memberTopic

  override def handle(message: MemberEvent)(implicit additionalData: RepComponents): Unit = {
    message match {
      case e: MemberCreated => handle(e)
      case e: MemberNameChanged => handle(e)
      case e: MemberEmailChanged => handle(e)
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
        MEMBERSHIP_MEMBERS.BECAME_MEMBER_AT
      )
      .values(
        e.id,
        e.name,
        e.email,
        MemberRole.intValueOf(e.role),
        e.becameMemberAt
      )
      .execute()
  }

  private def handle(e: MemberNameChanged)(implicit rc: RepComponents) = {
    rc.dsl
      .update(MEMBERSHIP_MEMBERS)
      .set(MEMBERSHIP_MEMBERS.NAME, e.name)
      .where(MEMBERSHIP_MEMBERS.ID.eq(e.id))
      .execute()
  }

  private def handle(e: MemberEmailChanged)(implicit rc: RepComponents) = {
    rc.dsl
      .update(MEMBERSHIP_MEMBERS)
      .set(MEMBERSHIP_MEMBERS.EMAIL, e.email)
      .where(MEMBERSHIP_MEMBERS.ID.eq(e.id))
      .execute()
  }

}
