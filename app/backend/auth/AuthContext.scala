package backend.auth

import backend.common.types.member.{MemberId, MemberRole}

case class AuthContext(currentMemberId: MemberId, currentMemberRole: MemberRole)
