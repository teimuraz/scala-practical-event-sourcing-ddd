package backend.membership.api.event

import javax.inject.Singleton
import library.messaging.Topic
import library.repository.RepComponents

@Singleton
class MemberEventTopic extends Topic[MemberEvent, RepComponents]

