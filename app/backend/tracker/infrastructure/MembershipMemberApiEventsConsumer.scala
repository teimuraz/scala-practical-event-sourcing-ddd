package backend.tracker.infrastructure

import backend.common.types.MemberId
import backend.membership.api.event.MemberEventTopic
import javax.inject.{Inject, Singleton}
import library.messaging.{Subscriber, Topic}
import library.repository.RepComponents
import backend.membership.api.{event => membershipApiEvent}
import backend.tracker.domain._
import library.error.InternalErrorException
import shapeless.Generic

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

    def updateTrackerMember(event: MemberDomainEvent) = {
      val member = getMember(event.id)
      member.applyNewChange(event)
      memberRepository.save(member)
    }

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
      case e =>
        val trackerEvent = e match {
            // TODO:: Use shapeless?
          case e: membershipApiEvent.MemberEmailChanged =>
            MemberEmailChanged(e.id, e.email)
          case e:membershipApiEvent.MemberNameChanged =>
            MemberNameChanged(e.id, e.name)
          case e: membershipApiEvent.MemberBecameAnOwner =>
            MemberBecameAnOwner(e.id, e.role)
          case e: membershipApiEvent.MemberBecameAStandardMember =>
            MemberBecameAStandardMember(e.id, e.role)
          case e: membershipApiEvent.MemberUnBecameAnOwner =>
            MemberUnBecameAnOwner(e.id, e.role)
          case e: membershipApiEvent.MemberDisconnected =>
            MemberDisconnected(e.id, e.role)
        }
        updateTrackerMember(trackerEvent)
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
