package backend.auth

import backend.common.MemberRole

case class AuthContext(currentMemberId: Long, currentMemberRole: MemberRole)
