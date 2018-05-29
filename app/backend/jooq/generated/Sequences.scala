/*
 * This file is generated by jOOQ.
*/
package backend.jooq.generated


import java.lang.Long

import javax.annotation.Generated

import org.jooq.Sequence
import org.jooq.impl.SequenceImpl

import scala.Array


/**
 * Convenience access to all sequences in public
 */
@Generated(
  value = Array(
    "http://www.jooq.org",
    "jOOQ version:3.10.5"
  ),
  comments = "This class is generated by jOOQ"
)
object Sequences {

  /**
   * The sequence <code>public.events_journal_event_offset_seq</code>
   */
  val EVENTS_JOURNAL_EVENT_OFFSET_SEQ : Sequence[Long] = new SequenceImpl[Long]("events_journal_event_offset_seq", Public.PUBLIC, org.jooq.impl.SQLDataType.BIGINT.nullable(false))

  /**
   * The sequence <code>public.membership_members_seq</code>
   */
  val MEMBERSHIP_MEMBERS_SEQ : Sequence[Long] = new SequenceImpl[Long]("membership_members_seq", Public.PUBLIC, org.jooq.impl.SQLDataType.BIGINT.nullable(false))

  /**
   * The sequence <code>public.membership_organizations_seq</code>
   */
  val MEMBERSHIP_ORGANIZATIONS_SEQ : Sequence[Long] = new SequenceImpl[Long]("membership_organizations_seq", Public.PUBLIC, org.jooq.impl.SQLDataType.BIGINT.nullable(false))

  /**
   * The sequence <code>public.tracker_issues_seq</code>
   */
  val TRACKER_ISSUES_SEQ : Sequence[Long] = new SequenceImpl[Long]("tracker_issues_seq", Public.PUBLIC, org.jooq.impl.SQLDataType.BIGINT.nullable(false))

  /**
   * The sequence <code>public.tracker_members_seq</code>
   */
  val TRACKER_MEMBERS_SEQ : Sequence[Long] = new SequenceImpl[Long]("tracker_members_seq", Public.PUBLIC, org.jooq.impl.SQLDataType.BIGINT.nullable(false))
}
