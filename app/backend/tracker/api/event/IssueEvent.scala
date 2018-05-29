package backend.tracker.api.event

import backend.common.types.Issue.{IssueDescription, IssueId, IssueStatus, IssueTitle}
import backend.common.types.member.MemberId
import library.eventsourcing.AggregateRootEvent
import org.joda.time.DateTime

trait IssueEvent extends AggregateRootEvent

case class IssueCreated(
  id: IssueId,
  title: IssueTitle,
  description: Option[IssueDescription],
  status: IssueStatus,
  assignee: List[MemberId],
  createdBy: MemberId,
  createdAt: DateTime
) extends IssueEvent

