package backend.membership.infrastructure

import backend.common.AggregateTypeRegistry
import backend.membership.domain.{Member, MemberDomainEvent, MemberId, MemberRepository}
import javax.inject
import javax.inject.Inject
import library.eventsourcing.{AggregateRootType, PgEventSourcedRepository}
import library.jooq.Db
import library.messaging.Topic
import library.repository.RepComponents
import play.api.libs.json.{Reads, Writes}
import backend.jooq.generated.Sequences.MEMBERSHIP_MEMBERS_SEQ
import scala.concurrent.Future
import backend.membership.domain.MemberDomainEventTopic

@inject.Singleton
class EventSourcedMemberRepository @Inject()
    (val db: Db, memberDomainEventTopic: MemberDomainEventTopic)
  extends MemberRepository with PgEventSourcedRepository[Member, MemberId, MemberDomainEvent]  {

  override def topic: Option[Topic[MemberDomainEvent, RepComponents]] = Some(memberDomainEventTopic)

  override def aggregateRootType: AggregateRootType = AggregateTypeRegistry.TYPE_MEMBERSHIP_MEMBER

  override def emptyState: Member = Member.empty

  override implicit def writes: Writes[MemberDomainEvent] = MemberDomainEvent.writes

  override implicit def reads: Reads[MemberDomainEvent] = MemberDomainEvent.reads


  override def idAsLong(id: MemberId): Long = id.value

  override def nextId: Future[MemberId] = {
    db.query { dsl =>
      val rawId = dsl.nextval(MEMBERSHIP_MEMBERS_SEQ)
      MemberId(rawId)
    }
  }
}


