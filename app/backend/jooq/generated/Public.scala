/*
 * This file is generated by jOOQ.
*/
package backend.jooq.generated


import backend.jooq.generated.tables.AggregateVersions
import backend.jooq.generated.tables.EventsJournal
import backend.jooq.generated.tables.MembershipMembers
import backend.jooq.generated.tables.MembershipOrganizations
import backend.jooq.generated.tables.PlayEvolutions
import backend.jooq.generated.tables.TrackerIssueAssignees
import backend.jooq.generated.tables.TrackerIssues
import backend.jooq.generated.tables.TrackerMembers

import java.util.ArrayList
import java.util.Arrays
import java.util.List

import javax.annotation.Generated

import org.jooq.Catalog
import org.jooq.Sequence
import org.jooq.Table
import org.jooq.impl.SchemaImpl

import scala.Array


object Public {

  /**
   * The reference instance of <code>public</code>
   */
  val PUBLIC = new Public
}

/**
 * This class is generated by jOOQ.
 */
@Generated(
  value = Array(
    "http://www.jooq.org",
    "jOOQ version:3.10.5"
  ),
  comments = "This class is generated by jOOQ"
)
class Public extends SchemaImpl("public", DefaultCatalog.DEFAULT_CATALOG) {

  override def getCatalog : Catalog = DefaultCatalog.DEFAULT_CATALOG

  override def getSequences : List[Sequence[_]] = {
    val result = new ArrayList[Sequence[_]]
    result.addAll(getSequences0)
    result
  }

  private def getSequences0(): List[Sequence[_]] = {
    return Arrays.asList[Sequence[_]](
      Sequences.EVENTS_JOURNAL_EVENT_OFFSET_SEQ,
      Sequences.MEMBERSHIP_MEMBERS_SEQ,
      Sequences.MEMBERSHIP_ORGANIZATIONS_SEQ,
      Sequences.TRACKER_ISSUES_SEQ,
      Sequences.TRACKER_MEMBERS_SEQ)
  }

  override def getTables : List[Table[_]] = {
    val result = new ArrayList[Table[_]]
    result.addAll(getTables0)
    result
  }

  private def getTables0(): List[Table[_]] = {
    return Arrays.asList[Table[_]](
      AggregateVersions.AGGREGATE_VERSIONS,
      EventsJournal.EVENTS_JOURNAL,
      MembershipMembers.MEMBERSHIP_MEMBERS,
      MembershipOrganizations.MEMBERSHIP_ORGANIZATIONS,
      PlayEvolutions.PLAY_EVOLUTIONS,
      TrackerIssueAssignees.TRACKER_ISSUE_ASSIGNEES,
      TrackerIssues.TRACKER_ISSUES,
      TrackerMembers.TRACKER_MEMBERS)
  }
}
