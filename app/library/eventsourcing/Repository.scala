package library.eventsourcing

import library.repository.RepComponents
import library.jooq.Transaction.TransactionBoundary

import scala.concurrent.Future

trait Repository[E <: AggregateRoot[E, ID, Event], ID, Event <: AggregateRootEvent] {
  def findById(id: ID): Future[Option[E]]
  def findByIdSync(id: ID)(implicit rc: RepComponents): Option[E]

  /**
   *
   * @param aggregateRoot
   * @param rc
   * @return Reset aggregate root (with empty uncommitted changes).
   */
  def save(aggregateRoot: E)(implicit rc: RepComponents): E
  def nextId: Future[ID]
}
