package backend.membership.api.impl

import backend.auth.AuthContext
import backend.common.Email
import backend.membership.api._
import backend.membership.domain.{Member, MemberId, MemberName, MemberRepository}
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
      member <- memberFuture.map {
        case Some(m) => m
        case None => throw new ValidationException(s"Cannot change name of non-existing member (member id ${req.id})")
      }
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
      member <- memberFuture.map {
        case Some(m) => m
        case None => throw new ValidationException(s"Cannot change email of non-existing member (member id ${req.id})")
      }
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
    val ownerFuture = memberRepository.findById(MemberId(context.currentMemberId))
    val standardMemberFuture = memberRepository.findById(MemberId(memberId))
    for {
      owner <- ownerFuture.map {
        case Some(owner) => owner
        case None => throw new ValidationException("Current member not found")
      }
      standardMember <- standardMemberFuture.map {
        case Some(member) => member
        case None => throw new ValidationException("Cannot make non existing member an owner")
      }
      newOwnerDto <- {
        standardMember.becomeAnOwner(owner) match {
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
