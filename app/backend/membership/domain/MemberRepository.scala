package backend.membership.domain

import backend.common.types.MemberId
import library.eventsourcing.Repository

trait MemberRepository extends Repository[Member, MemberId, MemberDomainEvent]
