package backend.membership.api

import javax.inject.Singleton
import library.messaging.Topic
import library.repository.RepComponents

@Singleton
class MemberApiEventTopic extends Topic[MemberApiEvent, RepComponents]
