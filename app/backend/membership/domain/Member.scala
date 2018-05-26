package backend.membership.domain

import backend.common.{Email, MemberRole, Owner, StandardMember}
import library.error.{ForbiddenException, ValidationException}
import library.eventsourcing.{AggregateRoot, AggregateRootInfo}
import library.validation.{DefaultMessage, StringValidatable}
import org.joda.time.DateTime
import play.api.libs.json._
import scala.util.Try

// TODO:: Better to use ADT instead of role param

case class Member private(
    id: MemberId,
    name: MemberName,
    email: Email,
    role: MemberRole,
    organizationId: OrganizationId,
    becameMemberAt: DateTime,
    aggregateRootInfo: AggregateRootInfo[MemberDomainEvent]
) extends AggregateRoot[Member, MemberId, MemberDomainEvent] {

  def changeName(newName: MemberName): Member = {
    applyNewChange(MemberNameChanged(id, newName))
  }

  def changeEmail(newEmail: Email): Member = {
    applyNewChange(MemberEmailChanged(id, newEmail))
  }

  def becomeAnOwner(initiator: Member): Try[Member] = Try({
    initiator.role match {
      case Owner => role match {
        case StandardMember => applyNewChange(MemberBecameAnOwner(id, Owner))
        case Owner => throw new ValidationException(s"Member $name is already an owner")
      }
      case _ => throw new ForbiddenException("Only owner can make another members as owners")
    }
  })

  def becomeAStandardMember(initiator: Member): Try[Member] = Try({
    initiator.role match {
      case Owner =>
        role match {
          case Owner =>
            val changes = List(
              MemberBecameAStandardMember(id, StandardMember),
              MemberUnBecameAnOwner(id, StandardMember)
            )
            applyNewChange(changes)
        case StandardMember => throw new ValidationException(s"Member ${name.value} is already a standard member")
      }
      case _ => throw new ForbiddenException("Only owner can make another members as standard member")
    }
  })

  def createNewMember(id: MemberId, name: MemberName, email: Email): Try[Member] = Try({
    role match {
      case Owner => Member.create(id, name, email, StandardMember, organizationId, DateTime.now())
      case _ => throw new ForbiddenException("Only owner can create new members")
    }
  })

  override def applyEvent(event: MemberDomainEvent): Member = {
    event match {
      case e: MemberCreated => Member(e.id, e.name, e.email, e.role, e.organizationId, e.becameMemberAt, aggregateRootInfo)
      case e: MemberNameChanged => copy(name = e.name)
      case e: MemberEmailChanged => copy(email = e.email)
      case e: MemberBecameAnOwner => copy(role = e.role)
      case e: MemberUnBecameAnOwner => this // do nothing
      case e: MemberBecameAStandardMember => copy(role = e.role)
    }
  }

  override def copyWithInfo(info: AggregateRootInfo[MemberDomainEvent]): Member = copy(aggregateRootInfo = info)
  override def idAsLong: Long = id.value
}

object Member {
  def create(id: MemberId, name: MemberName, email: Email, role: MemberRole, organizationId: OrganizationId, becameMemberAt: DateTime): Member = {
    val events = List(MemberCreated(id, name, email, role, organizationId, becameMemberAt))
    Member(id, name, email, role, organizationId, becameMemberAt, AggregateRootInfo(events, 0))
  }

  val empty: Member = Member(MemberId(0), MemberName("", validate = false), Email("", validate = false), StandardMember, OrganizationId(0), DateTime.now, AggregateRootInfo(Nil, 0))
}

/// Types

case class MemberId(value: Long) extends AnyVal

object MemberId {
  implicit val reads: Reads[MemberId] = Reads.of[Long].map(MemberId(_))
  implicit val writes: Writes[MemberId] = (o: MemberId) => JsNumber(o.value)
}

case class MemberName private(value: String) extends AnyVal

object MemberName extends StringValidatable[MemberName] {
  override def notEmpty = Some(DefaultMessage)
  override def inst = new MemberName(_)
  implicit val reads: Reads[MemberName] = Reads.of[String].map(MemberName(_))
  implicit val writes: Writes[MemberName] = (o: MemberName) => JsString(o.value)
}


