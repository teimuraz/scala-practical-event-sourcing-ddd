package backend.tracker.infrastructure

import java.lang

import backend.common.types.Issue.IssueStatus
import backend.tracker.api.event.{IssueCreated, IssueEvent}
import javax.inject.{Inject, Singleton}
import library.jooq.JooqRepositorySupport
import library.messaging.{Subscriber, Topic}
import library.repository.RepComponents
import backend.jooq.generated.Tables.TRACKER_ISSUES
import backend.jooq.generated.Tables.TRACKER_ISSUE_ASSIGNEES
import backend.jooq.generated.tables.records.TrackerIssueAssigneesRecord
import org.jooq.InsertValuesStep2

import scala.collection.immutable

@Singleton
class IssuesProjectionBuilder @Inject()(val topic: Topic[IssueEvent, RepComponents])
  extends Subscriber[IssueEvent, RepComponents]
  with JooqRepositorySupport {

  override def handle(message: IssueEvent)(implicit additionalData: RepComponents): Unit = {
    message match {
      case e: IssueCreated => handle(e)
    }
  }

  private def handle(e: IssueCreated)(implicit rc: RepComponents): Unit = {
    rc.dsl
      .insertInto(
        TRACKER_ISSUES,
        TRACKER_ISSUES.ID,
        TRACKER_ISSUES.TITLE,
        TRACKER_ISSUES.DESCRIPTION,
        TRACKER_ISSUES.STATUS,
        TRACKER_ISSUES.CREATED_BY,
        TRACKER_ISSUES.CREATED_AT
      )
      .values(
        e.id.value,
        e.title.value,
        e.description.map(_.value).orNull,
        IssueStatus.intValueOf(e.status),
        e.createdBy.value,
        e.createdAt
      )
      .execute()

    val assigneeQueries  = e.assignee.map { memberId =>
        rc.dsl
          .insertInto(
            TRACKER_ISSUE_ASSIGNEES, TRACKER_ISSUE_ASSIGNEES.ISSUE_ID, TRACKER_ISSUE_ASSIGNEES.MEMBER_ID
          )
          .values(e.id.value, memberId.value)
    }

    rc.dsl.batch(assigneeQueries:_*)
  }
}
