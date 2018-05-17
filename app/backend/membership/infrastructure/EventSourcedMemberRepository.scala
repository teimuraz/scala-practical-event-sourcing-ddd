package backend.membership.infrastructure

import backend.common.AggregateTypeRegistry
import backend.membership.domain.{Member, MemberEvent, MemberId, MemberRepository}
import javax.inject
import javax.inject.Inject
import library.eventsourcing.{AggregateRootType, PgEventSourcedRepository}
import library.jooq.Db
import play.api.libs.json.{Reads, Writes}

import scala.concurrent.Future

@inject.Singleton
class EventSourcedMemberRepository @Inject()
    (val db: Db, memberTopic: MemberTopic)
  extends MemberRepository with PgEventSourcedRepository[Member, MemberId, MemberEvent]  {

  override def aggregateRootType: AggregateRootType = AggregateTypeRegistry.TYPE_MEMBERSHIP_MEMBER

  override def emptyState: Member = Member.empty

  override implicit def writes: Writes[MemberEvent] = MemberEvent.writes

  override implicit def reads: Reads[MemberEvent] = MemberEvent.reads

  override def nextId: Future[MemberId] = {
//    db.query { dsl =>
//      val rawId: lang.Long = dsl.nextval(AM_PENDING_USERS_SEQ)
//      MemberId(rawId)
//    }
    ???
  }
}


