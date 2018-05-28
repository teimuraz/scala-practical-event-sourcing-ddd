package backend.tracker.infrastructure

import backend.common.AggregateTypeRegistry
import backend.common.types.MemberId
import backend.jooq.generated.Sequences.MEMBERSHIP_MEMBERS_SEQ
import backend.tracker.domain.{Member, MemberDomainEvent, MemberDomainEventTopic, MemberRepository}
import javax.inject
import javax.inject.Inject
import library.eventsourcing.{AggregateRootType, PgEventSourcedRepository}
import library.jooq.Db
import library.messaging.Topic
import library.repository.RepComponents
import play.api.libs.json.OFormat

import scala.concurrent.Future

@inject.Singleton
class EventSourcedMemberRepository @Inject()
    (val db: Db, memberDomainEventTopic: MemberDomainEventTopic)
  extends MemberRepository with PgEventSourcedRepository[Member, MemberId, MemberDomainEvent] {

  override def topic: Option[Topic[MemberDomainEvent, RepComponents]] = Some(memberDomainEventTopic)

  override def aggregateRootType: AggregateRootType = AggregateTypeRegistry.TYPE_TRACKER_MEMBER

  override def emptyState: Member = Member.empty

  override implicit def format: OFormat[MemberDomainEvent] = MemberDomainEvent.format

  override def idAsLong(id: MemberId): Long = id.value

  override def nextId: Future[MemberId] = {
    db.query { dsl =>
      val rawId = dsl.nextval(MEMBERSHIP_MEMBERS_SEQ)
      MemberId(rawId)
    }
  }
}


