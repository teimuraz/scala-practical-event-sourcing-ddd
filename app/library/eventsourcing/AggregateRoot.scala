package library.eventsourcing


trait DomainEvent

case class AggregateRootInfo[Event <: DomainEvent](uncommittedEvents: List[Event], version: Int)

object AggregateRootInfo {
}

trait AggregateRoot[E <: AggregateRoot[E, ID, Event], ID, Event <: DomainEvent] {

  def id: ID

  def applyEvent(event: Event): E

  def idAsLong: Long

  def aggregateRootInfo: AggregateRootInfo[Event]

  def copyWithInfo(info: AggregateRootInfo[Event]): E

  def applyNewChange(event: Event): E = {
    val newInfo: AggregateRootInfo[Event] = aggregateRootInfo.copy(uncommittedEvents = aggregateRootInfo.uncommittedEvents :+ event)
    val withAppliedEvent = this.applyEvent(event)
    withAppliedEvent.copyWithInfo(newInfo)
  }

  def applyNewChange(events: List[Event]): E = {
    val newInfo: AggregateRootInfo[Event] = aggregateRootInfo.copy(uncommittedEvents = aggregateRootInfo.uncommittedEvents ++ events)
    val withAppliedEvents = events.foldLeft(this)((state, e) => state.applyEvent(e))
    withAppliedEvents.copyWithInfo(newInfo)
  }

}
