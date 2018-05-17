package library.eventsourcing

import play.api.Logger

trait DomainEvent {
  def eventType: String
}

case class AggregateRootInfo[Event <: DomainEvent](uncommittedEvents: List[Event], version: Int)

object AggregateRootInfo {
}

trait AggregateRoot[E <: AggregateRoot[E, ID, Event], ID, Event <: DomainEvent] {

  def id: ID

  def applyEvent(event: Event): E

  def idAsLong: Long

  def aggregateRootInfo: AggregateRootInfo[Event]



  def copyWithInfo(info: AggregateRootInfo[Event]): E

  protected def applyChange(event: Event, isNew: Boolean = true): E = {
    if (isNew) {
      val newInfo: AggregateRootInfo[Event] = aggregateRootInfo.copy(uncommittedEvents = aggregateRootInfo.uncommittedEvents :+ event)
      val withAppliedEvent = this.applyEvent(event)
      withAppliedEvent.copyWithInfo(newInfo)
    } else {
      this.applyEvent(event)
    }
  }
}
