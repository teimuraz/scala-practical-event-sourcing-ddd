package backend.membership.infrastructure

import backend.membership.api.MemberApiEventTopic
import backend.membership.domain.{MemberCreated, MemberDomainEvent, MemberDomainEventTopic}
import javax.inject.{Inject, Singleton}
import library.messaging.{Subscriber, Topic}
import library.repository.RepComponents

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
    val apiEvent = message match {
      case e: MemberCreated
        => backend.membership.api.MemberCreated(e.id, e.name, e.email, e.role, e.becameMemberAt)
    }
  }
}
