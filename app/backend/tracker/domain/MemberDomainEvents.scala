package backend.tracker.domain

import library.eventsourcing.DomainEvent

trait MemberDomainEvent extends DomainEvent

case class MemberCreated extends MemberDomainEvent

