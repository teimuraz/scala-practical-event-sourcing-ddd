package backend.tracker.infrastructure

import backend.common.types.MemberId
import backend.membership.api.event.MemberEventTopic
import javax.inject.{Inject, Singleton}
import library.messaging.{Subscriber, Topic}
import library.repository.RepComponents
import backend.membership.api.{event => membershipApiEvent}
import backend.tracker.domain._
import library.error.InternalErrorException

/**
 * Subscribe to membership bounded contexts events
 */
@Singleton
class MembershipMemberApiEventsConsumer @Inject()
    (membershipMemberEventTopic: MemberEventTopic,
     memberRepository: MemberRepository)
  extends Subscriber[membershipApiEvent.MemberEvent, RepComponents] {

  override def topic: Topic[membershipApiEvent.MemberEvent, RepComponents] = membershipMemberEventTopic

  override def handle(message: membershipApiEvent.MemberEvent)(implicit additionalData: RepComponents): Unit = {
    message match {
      case e: membershipApiEvent.MemberCreated =>
        val member = Member(
          id = e.id,
          name = e.name,
          email = e.email,
          role = e.role,
          organizationId = e.organizationId,
          becameMemberAt = e.becameMemberAt
        )
        memberRepository.save(member)

      case e: membershipApiEvent.MemberEmailChanged =>
        val member = getMember(e.id)
        member.applyNewChange(MemberEmailChanged(e.id, e.email))
        memberRepository.save(member)

      case e: membershipApiEvent.MemberNameChanged =>
        val member = getMember(e.id)
        member.applyNewChange(MemberNameChanged(e.id, e.name))
        memberRepository.save(member)

      case e: membershipApiEvent.MemberDisconnected =>
        val member = getMember(e.id)
        member.applyNewChange(MemberDisconnected(e.id, e.role))
        memberRepository.save(member)


    }
  }

  private def getMember(id: MemberId)(implicit rc: RepComponents): Member = {
    memberRepository
    .findByIdSync(id)
    .getOrElse(
      throw new InternalErrorException(s"Something went wrong, no member with id ${id.value} exist in " +
        s"tracker bounded context")
    )

  }
}
