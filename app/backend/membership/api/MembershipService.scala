package backend.membership.api

import backend.auth.AuthContext
import backend.common.{Email, MemberRole}
import backend.membership.domain.MemberName
import org.joda.time.DateTime

import scala.concurrent.Future

case class CreateNewMemberReq(name: String, email: Email)
case class MemberDto(id: Long, name: String, email: String, role: MemberRole, becameMemberAt: DateTime)

trait MembershipService {
  def createNewMember(req: CreateNewMemberReq)(implicit context: AuthContext): Future[MemberDto]
}
