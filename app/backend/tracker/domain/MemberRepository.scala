package backend.tracker.domain

import backend.common.types.member.MemberId
import backend.tracker.api.event.MemberEvent
import library.eventsourcing.Repository

trait MemberRepository extends Repository[Member, MemberId, MemberEvent]
