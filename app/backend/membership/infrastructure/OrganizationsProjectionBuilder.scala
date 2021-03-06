package backend.membership.infrastructure

import backend.common.types.organization.OrganizationId
import backend.jooq.generated.Tables.MEMBERSHIP_ORGANIZATIONS
import backend.membership.api.event._
import backend.membership.domain._
import javax.inject.{Inject, Singleton}
import library.jooq.JooqRepositorySupport
import library.messaging.{Subscriber, Topic}
import library.repository.RepComponents

@Singleton
class OrganizationsProjectionBuilder @Inject()
    (val topic: Topic[OrganizationEvent, RepComponents])
  extends Subscriber[OrganizationEvent, RepComponents]
  with JooqRepositorySupport {

  override def handle(message: OrganizationEvent)(implicit additionalData: RepComponents): Unit = {
    message match {
      case e: OrganizationCreated => handle(e)
      case e: OwnersCountIncreased => handle(e)
      case e: OwnersCountDecreased => handle(e)
      case _ => ()
    }
  }

  private def handle(e: OrganizationCreated)(implicit rc: RepComponents): Unit = {
    rc.dsl
      .insertInto(
        MEMBERSHIP_ORGANIZATIONS,
        MEMBERSHIP_ORGANIZATIONS.ID,
        MEMBERSHIP_ORGANIZATIONS.NAME,
        MEMBERSHIP_ORGANIZATIONS.OWNERS_COUNT
      )
      .values(
        e.id.value,
        e.name.value,
        0
      )
      .execute()
  }

  private def handle(e: OwnersCountIncreased)(implicit rc: RepComponents) = {
    updateOwnersCount(e.id, e.totalOwnersCount)
  }

  private def handle(e: OwnersCountDecreased)(implicit rc: RepComponents) = {
    updateOwnersCount(e.id, e.totalOwnersCount)
  }

  private def updateOwnersCount(id: OrganizationId, ownersCount: Int)(implicit rc: RepComponents) = {
    rc.dsl
      .update(MEMBERSHIP_ORGANIZATIONS)
      .set(MEMBERSHIP_ORGANIZATIONS.OWNERS_COUNT, new Integer(ownersCount))
      .where(MEMBERSHIP_ORGANIZATIONS.ID.eq(id.value))
      .execute()
  }
}
