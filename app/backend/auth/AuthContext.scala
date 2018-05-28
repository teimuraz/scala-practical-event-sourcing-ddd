package backend.auth

import backend.common.types.member.MemberRole

case class AuthContext(currentMemberId: Long, currentMemberRole: MemberRole)
