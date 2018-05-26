package backend.tracker.infrastructure

import backend.membership.api.event.{MemberApiEvent, MemberApiEventTopic}
import javax.inject.{Inject, Singleton}
import library.messaging.{Subscriber, Topic}
import library.repository.RepComponents

/**
 * Subscribe to membership bounded contexts events
 */
@Singleton
class MembershipMemberApiEventsConsumer @Inject()
    (memberApiEventTopic: MemberApiEventTopic)
  extends Subscriber[MemberApiEvent, RepComponents] {

  override def topic: Topic[MemberApiEvent, RepComponents] = memberApiEventTopic

  override def handle(message: MemberApiEvent)(implicit additionalData: RepComponents): Unit = {

  }
}
