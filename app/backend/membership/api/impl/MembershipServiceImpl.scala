package backend.membership.api.impl

import backend.auth.AuthContext
import backend.common.Email
import backend.membership.api.{CreateNewMemberReq, MemberDto, MembershipService}
import backend.membership.domain.{Member, MemberId, MemberName, MemberRepository}
import javax.inject.{Inject, Singleton}
import library.error.ValidationException
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


  override def getMembers: Future[Seq[MemberDto]] = membershipQueryService.findAllMembers

  private def memberDomainToDto(member: Member): MemberDto = {
    MemberDto(member.id.value, member.name.value, member.email.value, member.role, member.becameMemberAt)
  }
}
