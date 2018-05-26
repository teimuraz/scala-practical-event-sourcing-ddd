package backend.membership.domain

import backend.common.types.{OrganizationId, OrganizationName}
import library.eventsourcing.DomainEvent
import play.api.libs.json._
import julienrf.json.derived.flat

sealed trait OrganizationDomainEvent extends DomainEvent

case class OrganizationCreated(id: OrganizationId, name: OrganizationName) extends OrganizationDomainEvent
object OrganizationCreated {
  implicit val format: OFormat[OrganizationCreated] = Json.format[OrganizationCreated]
}

case class OwnersCountIncreased(id: OrganizationId, totalOwnersCount: Int) extends OrganizationDomainEvent
object OwnersCountIncreased {
  implicit val format: OFormat[OwnersCountIncreased] = Json.format[OwnersCountIncreased]
}

case class OwnersCountDecreased(id: OrganizationId, totalOwnersCount: Int) extends OrganizationDomainEvent
object OwnersCountDecreased {
  implicit val format: OFormat[OwnersCountDecreased] = Json.format[OwnersCountDecreased]
}

object OrganizationDomainEvent {
  implicit lazy val format: OFormat[OrganizationDomainEvent] = flat.oformat((__ \ "eventType").format[String])
}
