package backend.membership.domain

import backend.common.types._
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
        case Owner => throw new ValidationException(s"Member $name is already an owner")
        case _ => applyNewChange(MemberBecameAnOwner(id, Owner))
      }
      case _ => throw new ForbiddenException("Only owner can make another members an owner")
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
          case FormerMember =>
            applyNewChange(MemberBecameAStandardMember(id, StandardMember))
        case StandardMember => throw new ValidationException(s"Member ${name.value} is already a standard member")
      }
      case _ => throw new ForbiddenException("Only owner can make another members as standard member")
    }
  })

  def disconnect(initiator: Member): Try[Member] = Try({
    initiator.role match {
      case Owner =>
        if (initiator.id == id) {
          throw new ForbiddenException("Member cannot disconnect itself")
        } else if (role == FormerMember){
          throw new ForbiddenException("Member is already disconnected")
        } else {
          val changes = List(
            Some(MemberDisconnected(id, FormerMember)),
            if (role == Owner) Some(MemberUnBecameAnOwner(id, Owner)) else None
          ).flatten
          applyNewChange(changes)
        }
      case _ =>
        throw new ForbiddenException("Only owner can disconnect another member")
    }
  })

  def createNewMember(id: MemberId, name: MemberName, email: Email): Try[Member] = Try({
    role match {
      case Owner => Member(id, name, email, StandardMember, organizationId, DateTime.now())
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
      case e: MemberDisconnected => copy(role = FormerMember)
    }
  }

  override def copyWithInfo(info: AggregateRootInfo[MemberDomainEvent]): Member = copy(aggregateRootInfo = info)
  override def idAsLong: Long = id.value
}

object Member {
  def apply(id: MemberId, name: MemberName, email: Email, role: MemberRole, organizationId: OrganizationId, becameMemberAt: DateTime): Member = {
    val events = List(MemberCreated(id, name, email, role, organizationId, becameMemberAt))
    Member(id, name, email, role, organizationId, becameMemberAt, AggregateRootInfo(events, 0))
  }

  val empty: Member = Member(MemberId(0), MemberName("", validate = false), Email("", validate = false), StandardMember, OrganizationId(0), DateTime.now, AggregateRootInfo(Nil, 0))
}










