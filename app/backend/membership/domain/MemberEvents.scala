package backend.membership.domain

import backend.common.Email
import library.eventsourcing.DomainEvent
import org.joda.time.DateTime
import play.api.libs.json._
import library.joda.json.jsonDateTimeFormat

trait MemberEvent extends DomainEvent

object MemberEvent {
  implicit val reads: Reads[MemberEvent] = {
    (__ \ "eventType").read[String].flatMap {
      case MemberCreated.eventType => implicitly[Reads[MemberCreated]].map(identity)
      case MemberNameChanged.eventType => implicitly[Reads[MemberNameChanged]].map(identity)
      case MemberEmailChanged.eventType => implicitly[Reads[MemberEmailChanged]].map(identity)
      case other => Reads(_ => JsError(s"Unknown event type $other"))
    }
  }
  implicit val writes: Writes[MemberEvent] = Writes { event =>
    val (jsValue, eventType) = event match {
      case m: MemberCreated => (Json.toJson(m)(MemberCreated.format), MemberCreated.eventType)
      case m: MemberNameChanged => (Json.toJson(m)(MemberNameChanged.format), MemberNameChanged.eventType)
      case m: MemberEmailChanged => (Json.toJson(m)(MemberEmailChanged.format), MemberEmailChanged.eventType)
    }
    jsValue.transform(__.json.update((__ \ 'eventType).json.put(JsString(eventType)))).get
  }
}

case class MemberCreated(
    id: MemberId,
    name: MemberName,
    email: Email,
    role: MemberRole,
    becameMemberAt: DateTime
) extends MemberEvent {
  override def eventType: String = MemberCreated.eventType
}

object MemberCreated {
  val eventType = "memberCreated"
  implicit val format: OFormat[MemberCreated] = Json.format[MemberCreated]
}

case class MemberNameChanged(id: MemberId, name: MemberName) extends MemberEvent {
  override def eventType: String = MemberNameChanged.eventType
}

object MemberNameChanged {
  val eventType = "memberNameChanged"
  implicit val format: OFormat[MemberNameChanged] = Json.format[MemberNameChanged]
}

case class MemberEmailChanged(id: MemberId, email: Email) extends MemberEvent {
  override def eventType: String = MemberEmailChanged.eventType
}

object MemberEmailChanged {
  val eventType = "memberEmailChanged"
  implicit val format: OFormat[MemberEmailChanged] = Json.format[MemberEmailChanged]
}
