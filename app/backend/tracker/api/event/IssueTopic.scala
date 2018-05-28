package backend.tracker.api.event

import javax.inject.Singleton
import library.messaging.Topic
import library.repository.RepComponents

@Singleton
class IssueTopic extends Topic[IssueEvent, RepComponents]
