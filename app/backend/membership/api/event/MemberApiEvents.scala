package backend.membership.api.event

import backend.common.types._
import org.joda.time.DateTime

trait MemberApiEvent

case class MemberCreated(
  id: MemberId,
  name: MemberName,
  email: Email,
  role: MemberRole,
  organizationId: OrganizationId,
  becameMemberAt: DateTime
) extends MemberApiEvent

case class MemberNameChanged(id: MemberId, name: MemberName) extends MemberApiEvent
case class MemberEmailChanged(id: MemberId, email: Email) extends MemberApiEvent
case class MemberDisconnected(id: MemberId) extends MemberApiEvent

