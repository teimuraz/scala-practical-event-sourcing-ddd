package backend.membership.domain

import library.error.ForbiddenException
import library.eventsourcing.{AggregateRoot, AggregateRootInfo}
import library.validation.{DefaultMessage, StringValidatable}
import play.api.libs.json.{JsNumber, JsString, Reads, Writes}

import scala.util.Try

case class Organization private(id: OrganizationId, name: OrganizationName, ownersCount: Int, aggregateRootInfo: AggregateRootInfo[OrganizationDomainEvent])
  extends AggregateRoot[Organization, OrganizationId, OrganizationDomainEvent] {

  def increaseOwnersCount: Organization = applyNewChange(OwnersCountIncreased(id, ownersCount + 1))

  def decreaseOwnersCount: Try[Organization] = Try({
    if (ownersCount == 1) {
      throw new ForbiddenException("Organization should have at least 1 owner")
    }
    applyNewChange(OwnersCountDecreased(id, ownersCount - 1))
  })

  override def applyEvent(event: OrganizationDomainEvent): Organization = {
    event match {
      case e: OrganizationCreated => Organization(e.id, e.name, 0, aggregateRootInfo)
      case e: OwnersCountIncreased => copy(ownersCount = e.totalOwnersCount)
      case e: OwnersCountDecreased => copy(ownersCount = e.totalOwnersCount)
    }
  }

  override def idAsLong: Long = id.value

  override def copyWithInfo(info: AggregateRootInfo[OrganizationDomainEvent]): Organization = copy(aggregateRootInfo = info)
}

object Organization {
  def create(id: OrganizationId, name: OrganizationName): Organization = {
    val events = List(OrganizationCreated(id, name))
    Organization(id, name, 0, AggregateRootInfo(events, 0))
  }

  val empty: Organization = Organization(OrganizationId(0), OrganizationName("", validate = false), 0, AggregateRootInfo(Nil, 0))
}

/// Types

case class OrganizationId(value: Long) extends AnyVal

object OrganizationId {
  implicit val reads: Reads[OrganizationId] = Reads.of[Long].map(OrganizationId(_))
  implicit val writes: Writes[OrganizationId] = (o: OrganizationId) => JsNumber(o.value)
}

case class OrganizationName(value: String) extends AnyVal

object OrganizationName extends StringValidatable[OrganizationName] {
  override def notEmpty = Some(DefaultMessage)
  override def inst = new OrganizationName(_)
  implicit val reads: Reads[OrganizationName] = Reads.of[String].map(OrganizationName(_))
  implicit val writes: Writes[OrganizationName] = (o: OrganizationName) => JsString(o.value)
}

