package backend.membership.infrastructure

import backend.common.AggregateTypeRegistry
import backend.common.types.member.MemberId
import backend.membership.domain.{Member, MemberRepository}
import javax.inject
import javax.inject.{Inject, Singleton}
import library.eventsourcing.{AggregateRootType, PgEventSourcedRepository}
import library.jooq.Db
import library.messaging.Topic
import library.repository.RepComponents
import play.api.libs.json.{OFormat, Reads, Writes}
import backend.jooq.generated.Sequences.MEMBERSHIP_MEMBERS_SEQ
import backend.membership.api.event.MemberEvent

import scala.concurrent.Future

@Singleton
class EventSourcedMemberRepository @Inject()
    (val db: Db, val memberEventTopic: Topic[MemberEvent, RepComponents])
  extends MemberRepository with PgEventSourcedRepository[Member, MemberId, MemberEvent] {

  override def topic: Option[Topic[MemberEvent, RepComponents]] = Some(memberEventTopic)

  override def aggregateRootType: AggregateRootType = AggregateTypeRegistry.TYPE_MEMBERSHIP_MEMBER

  override def emptyState: Member = Member.empty

  override implicit def format: OFormat[MemberEvent] = MemberEvent.format

  override def idAsLong(id: MemberId): Long = id.value

  override def nextId: Future[MemberId] = {
    db.query { dsl =>
      val rawId = dsl.nextval(MEMBERSHIP_MEMBERS_SEQ)
      MemberId(rawId)
    }
  }
}


