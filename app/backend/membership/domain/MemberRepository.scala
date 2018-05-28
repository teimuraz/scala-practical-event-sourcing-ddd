package backend.membership.domain

import backend.common.types.MemberId
import backend.membership.api.event.MemberEvent
import library.eventsourcing.Repository

trait MemberRepository extends Repository[Member, MemberId, MemberEvent]
