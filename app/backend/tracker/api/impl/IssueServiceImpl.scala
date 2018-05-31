package backend.tracker.api.impl

import backend.auth.AuthContext
import backend.common.types.Issue.{IssueDescription, IssueTitle}
import backend.common.types.member.MemberId
import backend.tracker.api.{CreateIssueReq, IssueDto, IssueService}
import backend.tracker.domain.{Issue, IssueRepository, MemberRepository}
import javax.inject.{Inject, Singleton}
import library.error.ValidationException
import library.jooq.TransactionManager

import scala.concurrent.Future

@Singleton
class IssueServiceImpl @Inject()
    (issueRepository: IssueRepository,
    memberRepository: MemberRepository,
    transactionManager: TransactionManager)
  extends IssueService {

  override def createIssue(req: CreateIssueReq)(implicit context: AuthContext): Future[IssueDto] = {
    // TODO:: check assignees existence
    val creatorFuture = memberRepository.findById(context.currentMemberId)
    val issueIdFuture = issueRepository.nextId
    for {
      creator <- creatorFuture
        .map(_.getOrElse(
          throw new ValidationException(s"Non existing member cannot create the issue " +
            s"(member id ${context.currentMemberId.value}")
        ))
      issueId <- issueIdFuture
      issueDto <- {
        val issue = creator.createIssue(
          issueId,
          IssueTitle(req.title),
          req.description.map(IssueDescription(_)),
          req.assignee.map(MemberId(_))
        )
        transactionManager.execute { implicit rc =>
          issueRepository.save(issue)
        }.map(_ =>
          IssueDto(
            issue.id.value,
            issue.title.value,
            issue.description.map(_.value),
            issue.assignee.map(_.value),
            issue.status,
            issue.createdBy.value,
            issue.createdAt
          )
        )
      }
    } yield {
      issueDto
    }

  }
}
