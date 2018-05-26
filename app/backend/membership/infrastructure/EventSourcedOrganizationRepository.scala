package backend.membership.infrastructure

import backend.common.AggregateTypeRegistry
import backend.common.types.OrganizationId
import backend.jooq.generated.Sequences.MEMBERSHIP_ORGANIZATIONS_SEQ
import backend.membership.domain._
import javax.inject.{Inject, Singleton}
import library.eventsourcing.{AggregateRootType, PgEventSourcedRepository}
import library.jooq.Db
import library.messaging.Topic
import library.repository.RepComponents
import play.api.libs.json.{Reads, Writes}

import scala.concurrent.Future


@Singleton
class EventSourcedOrganizationRepository @Inject()(val db: Db, organizationDomainEventTopic: OrganizationDomainEventTopic)
    extends OrganizationRepository
    with PgEventSourcedRepository[Organization, OrganizationId, OrganizationDomainEvent] {

  override def topic: Option[Topic[OrganizationDomainEvent, RepComponents]] = Some(organizationDomainEventTopic)

  override def aggregateRootType: AggregateRootType = AggregateTypeRegistry.TYPE_MEMBERSHIP_ORGANIZATION

  override def emptyState: Organization = Organization.empty

  override def idAsLong(id: OrganizationId): Long = id.value

  override implicit def writes: Writes[OrganizationDomainEvent] = OrganizationDomainEvent.writes

  override implicit def reads: Reads[OrganizationDomainEvent] = OrganizationDomainEvent.reads

  override def nextId: Future[OrganizationId] = {
    db.query { dsl =>
      val rawId = dsl.nextval(MEMBERSHIP_ORGANIZATIONS_SEQ)
      OrganizationId(rawId)
    }
  }
}
