package backend.tracker.domain

import backend.common.types.Issue.IssueId
import backend.tracker.api.event.IssueEvent
import library.eventsourcing.Repository

trait IssueRepository extends Repository[Issue, IssueId, IssueEvent]
