package backend.auth

import backend.common.types.MemberRole

case class AuthContext(currentMemberId: Long, currentMemberRole: MemberRole)
