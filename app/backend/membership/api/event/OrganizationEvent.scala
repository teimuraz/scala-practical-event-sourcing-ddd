package backend.membership.api.event

import backend.common.types.organization.{OrganizationId, OrganizationName}
import julienrf.json.derived.flat
import library.eventsourcing.AggregateRootEvent
import play.api.libs.json._

sealed trait OrganizationEvent extends AggregateRootEvent

case class OrganizationCreated(id: OrganizationId, name: OrganizationName) extends OrganizationEvent
object OrganizationCreated {
  implicit val format: OFormat[OrganizationCreated] = Json.format[OrganizationCreated]
}

case class OwnersCountIncreased(id: OrganizationId, totalOwnersCount: Int) extends OrganizationEvent
object OwnersCountIncreased {
  implicit val format: OFormat[OwnersCountIncreased] = Json.format[OwnersCountIncreased]
}

case class OwnersCountDecreased(id: OrganizationId, totalOwnersCount: Int) extends OrganizationEvent
object OwnersCountDecreased {
  implicit val format: OFormat[OwnersCountDecreased] = Json.format[OwnersCountDecreased]
}

object OrganizationEvent {
  implicit lazy val format: OFormat[OrganizationEvent] = flat.oformat((__ \ "eventType").format[String])
}
