package backend.membership.domain

import library.eventsourcing.DomainEvent
import play.api.libs.json._

trait OrganizationDomainEvent extends DomainEvent

object OrganizationDomainEvent {
  implicit val reads: Reads[OrganizationDomainEvent] = {
    (__ \ "eventType").read[String].flatMap {
      case OrganizationCreated.eventType => implicitly[Reads[OrganizationCreated]].map(identity)
      case OwnersCountIncreased.eventType => implicitly[Reads[OwnersCountIncreased]].map(identity)
      case OwnersCountDecreased.eventType => implicitly[Reads[OwnersCountDecreased]].map(identity)
      case other => Reads(_ => JsError(s"Unknown event type $other"))
    }
  }
  implicit val writes: Writes[OrganizationDomainEvent] = Writes { event =>
    val (jsValue, eventType) = event match {
      case m: OrganizationCreated => (Json.toJson(m)(OrganizationCreated.format), OrganizationCreated.eventType)
      case m: OwnersCountIncreased => (Json.toJson(m)(OwnersCountIncreased.format), OwnersCountIncreased.eventType)
      case m: OwnersCountDecreased => (Json.toJson(m)(OwnersCountDecreased.format), OwnersCountDecreased.eventType)
    }
    jsValue.transform(__.json.update((__ \ 'eventType).json.put(JsString(eventType)))).get
  }
}

case class OrganizationCreated(id: OrganizationId, name: OrganizationName) extends OrganizationDomainEvent {
  override def eventType: String = OrganizationCreated.eventType
}

object OrganizationCreated {
  implicit val format: OFormat[OrganizationCreated] = Json.format[OrganizationCreated]
  val eventType = "organizationCreated"
}

case class OwnersCountIncreased(id: OrganizationId, totalOwnersCount: Int) extends OrganizationDomainEvent {
  override def eventType: String = OwnersCountIncreased.eventType
}

object OwnersCountIncreased {
  implicit val format: OFormat[OwnersCountIncreased] = Json.format[OwnersCountIncreased]
  val eventType = "ownersCountIncreased"
}

case class OwnersCountDecreased(id: OrganizationId, totalOwnersCount: Int) extends OrganizationDomainEvent {
  override def eventType: String = OwnersCountDecreased.eventType
}

object OwnersCountDecreased {
  implicit val format: OFormat[OwnersCountDecreased] = Json.format[OwnersCountDecreased]
  val eventType = "ownersCountDecreassed"
}

