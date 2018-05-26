package backend.common

import library.eventsourcing.AggregateRootType

object AggregateTypeRegistry {
  val TYPE_MEMBERSHIP_MEMBER = AggregateRootType(1)
  val TYPE_MEMBERSHIP_ORGANIZATION = AggregateRootType(2)
}
