package backend.tracker.infrastructure

import backend.common.AggregateTypeRegistry
import backend.common.types.Issue.IssueId
import backend.common.types.member.MemberId
import backend.jooq.generated.Sequences.TRACKER_ISSUES_SEQ
import backend.tracker.api.event.{IssueEvent, MemberDomainEvent}
import backend.tracker.domain.{Issue, IssueRepository, Member, MemberRepository}
import javax.inject
import javax.inject.Inject
import library.eventsourcing.{AggregateRootType, PgEventSourcedRepository}
import library.jooq.Db
import play.api.libs.json.OFormat

import scala.concurrent.Future

@inject.Singleton
class EventSourcedIssueRepository @Inject()
    (val db: Db)
  extends IssueRepository with PgEventSourcedRepository[Issue, IssueId, IssueEvent] {

  override def aggregateRootType: AggregateRootType = AggregateTypeRegistry.TYPE_TRACKER_MEMBER

  override def emptyState: Issue = Issue.empty

  override implicit def format: OFormat[IssueEvent] = IssueEvent.format

  override def idAsLong(id: IssueId): Long = id.value

  override def nextId: Future[IssueId] = {
    db.query { dsl =>
      val rawId = dsl.nextval(TRACKER_ISSUES_SEQ)
      IssueId(rawId)
    }
  }
}


