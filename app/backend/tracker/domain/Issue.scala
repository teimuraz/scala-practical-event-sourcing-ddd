package backend.tracker.domain

import backend.common.types.Issue.{IssueDescription, IssueId, IssueStatus, IssueTitle}
import backend.common.types.member.MemberId
import backend.tracker.api.event.{IssueCreated, IssueEvent}
import library.eventsourcing.{AggregateRoot, AggregateRootInfo}
import org.joda.time.DateTime

case class Issue private(
  id: IssueId,
  title: IssueTitle,
  description: Option[IssueDescription],
  status: IssueStatus,
  assignee: List[MemberId],
  createdBy: MemberId,
  createdAt: DateTime,
  aggregateRootInfo: AggregateRootInfo[IssueEvent]
) extends AggregateRoot[Issue, IssueId, IssueEvent] {

  override def applyEvent(event: IssueEvent): Issue = ???

  override def idAsLong: Long = id.value

  override def copyWithInfo(info: AggregateRootInfo[IssueEvent]): Issue = copy(aggregateRootInfo = info)
}

object Issue {
  def apply(
    id: IssueId,
    title: IssueTitle,
    description: Option[IssueDescription],
    status: IssueStatus,
    assignee: List[MemberId],
    createdBy: MemberId,
    createdAt: DateTime
  ): Issue = {
    val events = List(IssueCreated(id, title, description, status, assignee, createdBy, createdAt))
    Issue(id, title, description, status, assignee, createdBy, createdAt, AggregateRootInfo(events, 0))
  }
}
