package backend.membership.infrastructure

import backend.membership.api.{MemberEvent, MemberTopic, MembershipService}
import javax.inject.{Inject, Singleton}
import library.messaging.{Subscriber, Topic}
import library.repository.RepComponents
import play.api.Logger

@Singleton
class MembersProjectionBuilder @Inject()
    (memberTopic: MemberTopic) extends Subscriber[MemberEvent, RepComponents]{

  override def topic: Topic[MemberEvent, RepComponents] = memberTopic

  override def handle(message: MemberEvent)(implicit additionalData: RepComponents): Unit = {
    Logger.info(s"Got $message")
  }
}
