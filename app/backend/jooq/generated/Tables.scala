/*
 * This file is generated by jOOQ.
*/
package backend.jooq.generated


import javax.annotation.Generated

import scala.Array


/**
 * Convenience access to all tables in public
 */
@Generated(
  value = Array(
    "http://www.jooq.org",
    "jOOQ version:3.10.5"
  ),
  comments = "This class is generated by jOOQ"
)
object Tables {

  /**
   * The table <code>public.aggregate_versions</code>.
   */
  val AGGREGATE_VERSIONS = backend.jooq.generated.tables.AggregateVersions.AGGREGATE_VERSIONS

  /**
   * The table <code>public.events_journal</code>.
   */
  val EVENTS_JOURNAL = backend.jooq.generated.tables.EventsJournal.EVENTS_JOURNAL

  /**
   * The table <code>public.membership_members</code>.
   */
  val MEMBERSHIP_MEMBERS = backend.jooq.generated.tables.MembershipMembers.MEMBERSHIP_MEMBERS

  /**
   * The table <code>public.play_evolutions</code>.
   */
  val PLAY_EVOLUTIONS = backend.jooq.generated.tables.PlayEvolutions.PLAY_EVOLUTIONS
}
