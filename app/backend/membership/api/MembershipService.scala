package backend.membership.api

import backend.auth.AuthContext
import backend.common.{Email, MemberRole}
import backend.membership.domain.MemberName
import org.joda.time.DateTime

import scala.concurrent.Future

case class CreateNewMemberReq(name: String, email: String)
case class ChangeMemberNameReq(id: Long, name: String)
case class ChangeMemberEmailReq(id: Long, email: String)

case class MemberDto(id: Long, name: String, email: String, role: MemberRole, becameMemberAt: DateTime)

trait MembershipService {
  def createNewMember(req: CreateNewMemberReq)(implicit context: AuthContext): Future[MemberDto]
  def changeMemberName(req: ChangeMemberNameReq): Future[MemberDto]
  def changeMemberEmail(req: ChangeMemberEmailReq): Future[MemberDto]
  def makeMemberAnOwner(memberId: Long)(implicit context: AuthContext): Future[MemberDto]
  def getMembers: Future[Seq[MemberDto]]
  def getMember(id: Long): Future[MemberDto]
}
