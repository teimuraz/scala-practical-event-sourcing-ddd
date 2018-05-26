package backend.membership.domain

import javax.inject.Singleton
import library.messaging.Topic
import library.repository.RepComponents

@Singleton
class OrganizationDomainEventTopic extends Topic[OrganizationDomainEvent, RepComponents]

