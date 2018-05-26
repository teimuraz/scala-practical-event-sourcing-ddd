package library.eventsourcing

import library.jooq.{Db, JooqRepositorySupport}
import org.jooq.DSLContext

import scala.collection.mutable
import scala.concurrent.Future
import backend.jooq.generated.tables.EventsJournal.EVENTS_JOURNAL
import backend.jooq.generated.tables.records.EventsJournalRecord
import library.repository.RepComponents
import library.jooq.Transaction.TransactionBoundary
import play.api.libs.json.{Json, OFormat, Reads, Writes}
import backend.jooq.generated.tables.AggregateVersions.AGGREGATE_VERSIONS
import library.messaging.Topic

case class AggregateRootType(value: Int) extends AnyVal

trait PgEventSourcedRepository[E <: AggregateRoot[E, ID, Event], ID, Event <: DomainEvent] extends Repository[E, ID, Event] with JooqRepositorySupport {

  def aggregateRootType: AggregateRootType
  def emptyState: E
  def db: Db
  def topic: Option[Topic[Event, RepComponents]] = None
  def idAsLong(id: ID): Long

  implicit def writes: Writes[Event]
  implicit def reads: Reads[Event]

  override def findById(id: ID): Future[Option[E]] = {
    db.query { dsl =>
      doFindById(id, dsl)
    }
  }

  override def findByIdSync(id: ID)(implicit rc: RepComponents): Option[E] = doFindById(id, rc.dsl)

  private def doFindById(id: ID, dsl: DSLContext): Option[E] = {
    import scala.collection.JavaConverters._
    val eventRecords = dsl
      .selectFrom(EVENTS_JOURNAL)
      .where(
        EVENTS_JOURNAL.AGGREGATE_ROOT_TYPE.eq(aggregateRootType.value)
          // let's assume that all ids
          .and(EVENTS_JOURNAL.AGGREGATE_ROOT_ID.eq(idAsLong(id)))
      )
      .orderBy(EVENTS_JOURNAL.EVENT_OFFSET)
      .fetchInto(classOf[EventsJournalRecord])
      .asScala

    if (eventRecords.isEmpty) {
      None
    } else {
      val aggregateRoot: E = eventRecords
        .foldLeft(emptyState) { (state, record) =>
          val eventRaw = Json.parse(record.getEvent.toString)
          val event = eventRaw.as[Event]
          state.applyEvent(event)
        }

      val withVersion = aggregateRoot.copyWithInfo(info = AggregateRootInfo(Nil, eventRecords.last.getAggregateVersion))
      Some(withVersion)
    }
  }

  override def save(aggregateRoot: E)(implicit rc: RepComponents): E = {
    var version = aggregateRoot.aggregateRootInfo.version
    aggregateRoot.aggregateRootInfo.uncommittedEvents.foreach { e =>
      version = version + 1
      val event = Json.toJson(e)
      val eventType = e.eventType
      rc.dsl
        .insertInto(EVENTS_JOURNAL,
          EVENTS_JOURNAL.AGGREGATE_ROOT_TYPE,
          EVENTS_JOURNAL.AGGREGATE_ROOT_ID,
          EVENTS_JOURNAL.EVENT_TYPE,
          EVENTS_JOURNAL.EVENT,
          EVENTS_JOURNAL.AGGREGATE_VERSION
        )
        .values(
          aggregateRootType.value,
          aggregateRoot.idAsLong,
          eventType,
          event,
          version
        )
        .execute()
    }

    // Optimistic lock
    val updatedRows: Int = rc.dsl
      .update(AGGREGATE_VERSIONS)
      .set(AGGREGATE_VERSIONS.CURRENT_VERSION, new Integer(version))
      .where(
        AGGREGATE_VERSIONS.AGGREGATE_ROOT_TYPE.eq(aggregateRootType.value)
          .and(AGGREGATE_VERSIONS.AGGREGATE_ROOT_ID.eq(aggregateRoot.idAsLong))
          .and(AGGREGATE_VERSIONS.CURRENT_VERSION.eq(aggregateRoot.aggregateRootInfo.version))
      )
      .execute()

    if (updatedRows == 0) {
      // No record for given aggregate root type with given id and expected version?
      // Then try to create this record.
      // If record creation will fail (since there is a primary key which is composed of aggregate_root_type
      // and aggregate_root_id), then this means that record exist and versions do not match, therefore aggregate
      // was modified between it was loaded and save attempt.

      try {
        rc.dsl
          .insertInto(
            AGGREGATE_VERSIONS,
            AGGREGATE_VERSIONS.AGGREGATE_ROOT_TYPE,
            AGGREGATE_VERSIONS.AGGREGATE_ROOT_ID,
            AGGREGATE_VERSIONS.CURRENT_VERSION
          )
          .values(aggregateRootType.value,
            aggregateRoot.idAsLong,
            new Integer(version)
          )
          .execute()
      } catch {
        case ex: org.jooq.exception.DataAccessException =>
          if (ex.getMessage.contains("ERROR: duplicate key")) {
            // Yes, the raw really exists, that's mean concurrency issue
            throw new ConcurrencyException(s"Aggregate root of type $aggregateRootType with id ${aggregateRoot.id} with " +
              s"loaded version ${aggregateRoot.aggregateRootInfo.version} has been modified between it was loaded and" +
              s" save attempt.")
          }
      }
    }

    // Publish event in the same thread and transaction (if topic provided)
    topic.foreach { t =>
      aggregateRoot.aggregateRootInfo.uncommittedEvents.foreach(t.publish)
    }

    val newInfo: AggregateRootInfo[Event] = aggregateRoot.aggregateRootInfo.copy(version = version, uncommittedEvents = Nil)
    aggregateRoot.copyWithInfo(newInfo)
  }
}