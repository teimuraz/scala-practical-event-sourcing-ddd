package backend.tracker.api

import backend.auth.AuthContext
import backend.common.types.Issue.{IssueDescription, IssueId, IssueStatus, IssueTitle}
import org.joda.time.DateTime

import scala.concurrent.Future

trait IssueService {
  def createIssue(req: CreateIssueReq)(implicit context: AuthContext): Future[IssueDto]
}

case class CreateIssueReq(
  title: String,
  description: Option[String],
  assignee: List[Long]
)

case class IssueDto(
  id: Long,
  title: String,
  description: Option[String],
  assignee: List[Long],
  status: IssueStatus,
  createdBy: Long,
  createdAt: DateTime
)
