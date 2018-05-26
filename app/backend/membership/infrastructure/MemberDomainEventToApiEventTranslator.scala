package backend.membership.infrastructure

import backend.membership.api.event.MemberApiEventTopic
import backend.membership.domain._
import javax.inject.{Inject, Singleton}
import library.messaging.{Subscriber, Topic}
import library.repository.RepComponents
import backend.membership.api.{event => apiEvent}

@Singleton
class MemberDomainEventToApiEventTranslator @Inject()
  (memberApiEventTopic: MemberApiEventTopic,
  memberDomainEventTopic: MemberDomainEventTopic)
  extends Subscriber[MemberDomainEvent, RepComponents] {
  override def topic: Topic[MemberDomainEvent, RepComponents] = memberDomainEventTopic

  /**
   * Handle event in the same thread it was published
   *
   * @param message
   * @param additionalData
   */
  override def handle(message: MemberDomainEvent)(implicit additionalData: RepComponents): Unit = {
    val evt = message match {
      case e: MemberCreated
        => Some(apiEvent.MemberCreated(e.id, e.name, e.email, e.role, e.becameMemberAt))
      case e: MemberNameChanged
        => Some(apiEvent.MemberNameChanged(e.id, e.name))
      case e: MemberEmailChanged
        => Some(apiEvent.MemberEmailChanged(e.id, e.email))
      case _ => None
    }

    evt.foreach(memberApiEventTopic.publish(_))
  }
}
