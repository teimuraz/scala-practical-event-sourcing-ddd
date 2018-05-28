package backend.tracker.infrastructure

import backend.membership.api.event.{MemberApiEvent, MemberApiEventTopic, MemberCreated}
import javax.inject.{Inject, Singleton}
import library.messaging.{Subscriber, Topic}
import library.repository.RepComponents
import backend.membership.api.{event => apiEvent}
import backend.tracker.domain.{Member, MemberRepository}

/**
 * Subscribe to membership bounded contexts events
 */
@Singleton
class MembershipMemberApiEventsConsumer @Inject()
    (memberApiEventTopic: MemberApiEventTopic,
     memberRepository: MemberRepository)
  extends Subscriber[MemberApiEvent, RepComponents] {

  override def topic: Topic[MemberApiEvent, RepComponents] = memberApiEventTopic

  override def handle(message: MemberApiEvent)(implicit additionalData: RepComponents): Unit = {
    message match {
      case e: apiEvent.MemberCreated =>
        val member = Member(
          id = e.id,
          name = e.name,
          email = e.email,
          role = e.role,
          organizationId = e.organizationId,
          becameMemberAt = e.becameMemberAt
        )
        memberRepository.save(member)
      case e: apiEvent.MemberEmailChanged =>

    }
  }
}
