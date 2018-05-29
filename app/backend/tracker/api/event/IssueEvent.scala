package backend.tracker.api.event

import backend.common.types.Issue.{IssueDescription, IssueId, IssueStatus, IssueTitle}
import backend.common.types.member.MemberId
import library.eventsourcing.AggregateRootEvent
import org.joda.time.DateTime
import play.api.libs.json.{Json, OFormat, __}
import julienrf.json.derived.flat
import library.joda.json.jsonDateTimeFormat

sealed trait IssueEvent extends AggregateRootEvent

case class IssueCreated(
  id: IssueId,
  title: IssueTitle,
  description: Option[IssueDescription],
  status: IssueStatus,
  assignee: List[MemberId],
  createdBy: MemberId,
  createdAt: DateTime
) extends IssueEvent

object IssueCreated {
  implicit val format: OFormat[IssueCreated] = Json.format[IssueCreated]
}


object IssueEvent {
  implicit lazy val format: OFormat[IssueEvent] = flat.oformat((__ \ "eventType").format[String])
}
