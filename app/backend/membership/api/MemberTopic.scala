package backend.membership.api

import library.messaging.Topic
import library.repository.RepComponents

class MemberTopic extends Topic[MemberEvent, RepComponents]
