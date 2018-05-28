package backend.membership.api.impl

import backend.auth.AuthContext
import backend.common.types.member.{MemberId, MemberName}
import backend.common.types.Email
import backend.membership.api._
import backend.membership.domain.{Member, MemberRepository}
import javax.inject.{Inject, Singleton}
import library.error.{ForbiddenException, NotFoundException, ValidationException}
import library.jooq.TransactionManager

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class MembershipServiceImpl @Inject()
    (memberRepository: MemberRepository,
     membershipQueryService: MembershipQueryService)
    (transactionManager: TransactionManager)
    (implicit ec: ExecutionContext)
  extends MembershipService {

  override def createNewMember(req: CreateNewMemberReq)(implicit context: AuthContext): Future[MemberDto] = {
    val memberWithSameEmailFuture = membershipQueryService.findByEmail(req.email)
    val membershipWithSameNameFuture = membershipQueryService.findByName(req.name)
    val creatorFuture = memberRepository.findById(MemberId(context.currentMemberId))

    for {
      _ <- memberWithSameEmailFuture
        .map { memberOpt =>
          memberOpt.foreach(_ => throw new ValidationException(s"Email ${req.email} is already taken"))
        }
      _ <- membershipWithSameNameFuture
        .map { memberOpt =>
          memberOpt.foreach(_ => throw new ValidationException(s"Name ${req.name} is already taken"))
        }
      creator <- creatorFuture.map {
        case Some(member) => member
        case None => throw new ValidationException("Ghost cannot create a member")
      }
      newMemberId <- memberRepository.nextId
      memberDto <- {
        creator.createNewMember(newMemberId, MemberName(req.name), Email(req.email)) match {
          case Success(newMember) =>
            transactionManager.execute { implicit rc =>
              memberRepository.save(newMember)
            }
            .map(memberDomainToDto)
          case Failure(ex) => Future.failed(ex)
        }
      }

    } yield {
      memberDto
    }
  }

  override def changeMemberName(req: ChangeMemberNameReq): Future[MemberDto] = {
    val memberWithSameNameFuture = membershipQueryService.findByName(req.name)
    val memberFuture = memberRepository.findById(MemberId(req.id))
    for {
      _ <- memberWithSameNameFuture.map { memberOpt =>
          memberOpt.foreach { member =>
            if (member.id != req.id) {
              throw new ValidationException(s"Name ${req.name} is already taken")
            }
          }
        }
      member <- memberFuture
        .map(_.getOrElse(throw new ValidationException(s"Cannot change name of non-existing member (member id ${req.id})")))
      memberDto <- {
        val withChangedName = member.changeName(MemberName(req.name))
        transactionManager.execute { implicit rc =>
          memberRepository.save(withChangedName)
        }.map(memberDomainToDto)
      }
    } yield {
      memberDto
    }
  }

  override def changeMemberEmail(req: ChangeMemberEmailReq): Future[MemberDto] = {
    val memberWithSameEmailFuture = membershipQueryService.findByName(req.email)
    val memberFuture = memberRepository.findById(MemberId(req.id))
    for {
      _ <- memberWithSameEmailFuture.map { memberOpt =>
        memberOpt.foreach { member =>
          if (member.id != req.id) {
            throw new ValidationException(s"Email ${req.email} is already taken")
          }
        }
      }
      member <- memberFuture
        .map(_.getOrElse(throw new ValidationException(s"Cannot change email of non-existing member (member id ${req.id})")))
      memberDto <- {
        val withChangedEmail = member.changeEmail(Email(req.email))
        transactionManager.execute { implicit rc =>
          memberRepository.save(withChangedEmail)
        }.map(memberDomainToDto)
      }
    } yield {
      memberDto
    }
  }


  override def makeMemberAnOwner(memberId: Long)(implicit context: AuthContext): Future[MemberDto] = {
    val initiatorFuture = memberRepository.findById(MemberId(context.currentMemberId))
    val standardMemberFuture = memberRepository.findById(MemberId(memberId))
    for {
      initiator <- initiatorFuture
        .map(_.getOrElse(throw new ValidationException(s"Initiator with id ${context.currentMemberId} not found")))
      standardMember <- standardMemberFuture
        .map(_.getOrElse(throw new ValidationException(s"Cannot make non existing member an owner (member id: $memberId)")))
      newOwnerDto <- {
        standardMember.becomeAnOwner(initiator) match {
          case Success(newOwner) =>
            transactionManager.execute { implicit rc =>
              memberRepository.save(newOwner)
            }
            .map(memberDomainToDto)
          case Failure(e) => Future.failed(e)
        }
      }
    } yield {
      newOwnerDto
    }
  }

  override def makeMemberAStandardMember(memberId: Long)(implicit context: AuthContext): Future[MemberDto] = {
    val initiatorFuture = memberRepository.findById(MemberId(context.currentMemberId))
    val targetOwnerFuture = memberRepository.findById(MemberId(memberId))
    for {
      initiator <- initiatorFuture
        .map(_.getOrElse(throw new ValidationException(s"Initiator with id ${context.currentMemberId} not found")))
      targetOwner <- targetOwnerFuture
        .map(_.getOrElse(throw new ValidationException(s"Cannot make non existing member a standard member (member id: $memberId)")))
      standardMemberDto <- {
        targetOwner.becomeAStandardMember(initiator) match {
          case Success(standardMember) =>
            transactionManager.execute { implicit rc =>
              memberRepository.save(standardMember)
            }
            .map(memberDomainToDto)
          case Failure(e) => Future.failed(e)
        }
      }
    } yield {
      standardMemberDto
    }
  }

  override def disconnectMember(memberId: Long)(implicit context: AuthContext): Future[MemberDto] = {
    val initiatorFuture = memberRepository.findById(MemberId(context.currentMemberId))
    val targetMemberFuture = memberRepository.findById(MemberId(memberId))
    for {
      initiator <- initiatorFuture
        .map(_.getOrElse(throw new ValidationException(s"Initiator with id ${context.currentMemberId} not found")))
      targetMember: Member <- targetMemberFuture
        .map(_.getOrElse(throw new ValidationException(s"Cannot disconnect non existing member (member id $memberId)")))
      disconnectedMemberDto <- {
        targetMember.disconnect(initiator) match {
          case Success(disconnectedMember) =>
            transactionManager.execute { implicit rc =>
              memberRepository.save(disconnectedMember)
            }
            .map(memberDomainToDto)
          case Failure(ex) => Future.failed(ex)
        }
      }
    } yield {
      disconnectedMemberDto
    }
  }

  override def getMembers: Future[Seq[MemberDto]] = membershipQueryService.findAllMembers

  override def getMember(id: Long): Future[MemberDto] = {
    membershipQueryService.findById(id).map {
      case Some(member) => member
      case None => throw new NotFoundException(s"Member with id $id not found")
    }
  }

  private def memberDomainToDto(member: Member): MemberDto = {
    MemberDto(member.id.value, member.name.value, member.email.value, member.role, member.becameMemberAt)
  }




}
