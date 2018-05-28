package backend.tracker.domain

import backend.common.types.member.MemberId
import library.eventsourcing.Repository

trait MemberRepository extends Repository[Member, MemberId, MemberDomainEvent]
