package backend.membership.domain

import library.eventsourcing.Repository

trait MemberRepository extends Repository[Member, MemberId, MemberDomainEvent]
