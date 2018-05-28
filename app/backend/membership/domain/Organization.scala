package backend.membership.domain

import backend.common.types.{OrganizationId, OrganizationName}
import backend.membership.api.event.{OrganizationCreated, OrganizationEvent, OwnersCountDecreased, OwnersCountIncreased}
import library.error.ForbiddenException
import library.eventsourcing.{AggregateRoot, AggregateRootInfo}

import scala.util.Try

case class Organization private(id: OrganizationId, name: OrganizationName, ownersCount: Int, aggregateRootInfo: AggregateRootInfo[OrganizationEvent])
  extends AggregateRoot[Organization, OrganizationId, OrganizationEvent] {

  def increaseOwnersCount: Organization = applyNewChange(OwnersCountIncreased(id, ownersCount + 1))

  def decreaseOwnersCount: Try[Organization] = Try({
    if (ownersCount == 1) {
      throw new ForbiddenException("Organization should have at least 1 owner")
    }
    applyNewChange(OwnersCountDecreased(id, ownersCount - 1))
  })

  override def applyEvent(event: OrganizationEvent): Organization = {
    event match {
      case e: OrganizationCreated => Organization(e.id, e.name, 0, aggregateRootInfo)
      case e: OwnersCountIncreased => copy(ownersCount = e.totalOwnersCount)
      case e: OwnersCountDecreased => copy(ownersCount = e.totalOwnersCount)
    }
  }

  override def idAsLong: Long = id.value

  override def copyWithInfo(info: AggregateRootInfo[OrganizationEvent]): Organization = copy(aggregateRootInfo = info)
}

object Organization {
  def create(id: OrganizationId, name: OrganizationName): Organization = {
    val events = List(OrganizationCreated(id, name))
    Organization(id, name, 0, AggregateRootInfo(events, 0))
  }

  val empty: Organization = Organization(OrganizationId(0), OrganizationName("", validate = false), 0, AggregateRootInfo(Nil, 0))
}
