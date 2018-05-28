package backend.tracker.domain

import javax.inject.Singleton
import library.messaging.Topic
import library.repository.RepComponents

@Singleton
class MemberDomainEventTopic extends Topic[MemberDomainEvent, RepComponents]

