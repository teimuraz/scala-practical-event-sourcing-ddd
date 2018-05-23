package backend.membership.api

import backend.common.MemberRole
import org.joda.time.DateTime

trait MemberEvent

case class MemberCreated(
  id: Long,
  name: String,
  email: String,
  role: MemberRole,
  becameMemberAt: DateTime
) extends MemberEvent

case class MemberNameChanged(id: Long, name: String) extends MemberEvent

case class MemberEmailChanged(id: Long, email: String) extends MemberEvent

