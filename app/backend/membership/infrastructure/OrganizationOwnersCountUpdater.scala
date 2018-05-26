package backend.membership.infrastructure

import backend.common.Owner
import backend.membership.domain._
import javax.inject.{Inject, Singleton}
import library.error.InternalErrorException
import library.messaging.{Subscriber, Topic}
import library.repository.RepComponents

import scala.util.{Failure, Success}

@Singleton
class OrganizationOwnersCountUpdater @Inject()
    (memberDomainEventTopic: MemberDomainEventTopic,
    organizationRepository: OrganizationRepository,
    memberRepository: MemberRepository)
  extends Subscriber[MemberDomainEvent, RepComponents] {
  override def topic: Topic[MemberDomainEvent, RepComponents] = memberDomainEventTopic

  /**
   * Handle event in the same thread it was published
   *
   * @param message
   * @param additionalData
   */
  override def handle(message: MemberDomainEvent)(implicit additionalData: RepComponents): Unit = {
    message match {
      case e: MemberCreated => if (e.role == Owner) {
        changeOrganizationOwnersCount(e.id, Increase)
      }
      case e: MemberBecameAnOwner => changeOrganizationOwnersCount(e.id, Increase)
      case e: MemberUnBecameAnOwner => changeOrganizationOwnersCount(e.id, Decrease)
      case _ => ()
    }
  }

  sealed trait OwnersCountOperation
  case object Increase extends OwnersCountOperation
  case object Decrease extends OwnersCountOperation

  private def changeOrganizationOwnersCount(memberId: MemberId, op: OwnersCountOperation)(implicit additionalData: RepComponents) = {
    memberRepository.findByIdSync(memberId) match {
      case Some(member) =>
        organizationRepository.findByIdSync(member.organizationId) match {
          case Some(organization) =>
            val organizationUpdated = op match {
              case Increase => organization.increaseOwnersCount
              case Decrease => organization.decreaseOwnersCount match {
                case Success(org) => org
                case Failure(ex) => throw ex
              }
            }
            organizationRepository.save(organizationUpdated)
          case None =>
            throw new InternalErrorException(s"No organization with id ${member.organizationId.value} found" +
              s" while updating organization's owner count")
        }
      case None =>
        throw new InternalErrorException(s"No member with id ${memberId.value} found" +
          s" while updating organization's owner count")
    }
  }
}
