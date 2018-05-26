/*
 * This file is generated by jOOQ.
*/
package backend.jooq.generated


import backend.jooq.generated.tables.AggregateVersions
import backend.jooq.generated.tables.EventsJournal
import backend.jooq.generated.tables.MembershipMembers
import backend.jooq.generated.tables.MembershipOrganizations
import backend.jooq.generated.tables.PlayEvolutions
import backend.jooq.generated.tables.records.AggregateVersionsRecord
import backend.jooq.generated.tables.records.EventsJournalRecord
import backend.jooq.generated.tables.records.MembershipMembersRecord
import backend.jooq.generated.tables.records.MembershipOrganizationsRecord
import backend.jooq.generated.tables.records.PlayEvolutionsRecord

import java.lang.Long

import javax.annotation.Generated

import org.jooq.Identity
import org.jooq.UniqueKey
import org.jooq.impl.Internal

import scala.Array


/**
 * A class modelling foreign key relationships and constraints of tables of 
 * the <code>public</code> schema.
 */
@Generated(
  value = Array(
    "http://www.jooq.org",
    "jOOQ version:3.10.5"
  ),
  comments = "This class is generated by jOOQ"
)
object Keys {

  // -------------------------------------------------------------------------
  // IDENTITY definitions
  // -------------------------------------------------------------------------

  val IDENTITY_EVENTS_JOURNAL = Identities0.IDENTITY_EVENTS_JOURNAL

  // -------------------------------------------------------------------------
  // UNIQUE and PRIMARY KEY definitions
  // -------------------------------------------------------------------------

  val AGGREGATE_VERSIONS_PK = UniqueKeys0.AGGREGATE_VERSIONS_PK
  val EVENTS_JOURNAL_PK = UniqueKeys0.EVENTS_JOURNAL_PK
  val MEMBERSHIP_MEMBERS_PKEY = UniqueKeys0.MEMBERSHIP_MEMBERS_PKEY
  val MEMBERSHIP_ORGANIZATIONS_PKEY = UniqueKeys0.MEMBERSHIP_ORGANIZATIONS_PKEY
  val PLAY_EVOLUTIONS_PKEY = UniqueKeys0.PLAY_EVOLUTIONS_PKEY

  // -------------------------------------------------------------------------
  // FOREIGN KEY definitions
  // -------------------------------------------------------------------------


  // -------------------------------------------------------------------------
  // [#1459] distribute members to avoid static initialisers > 64kb
  // -------------------------------------------------------------------------

  private object Identities0 {
    val IDENTITY_EVENTS_JOURNAL : Identity[EventsJournalRecord, Long] = Internal.createIdentity(EventsJournal.EVENTS_JOURNAL, EventsJournal.EVENTS_JOURNAL.EVENT_OFFSET)
  }

  private object UniqueKeys0 {
    val AGGREGATE_VERSIONS_PK : UniqueKey[AggregateVersionsRecord] = Internal.createUniqueKey(AggregateVersions.AGGREGATE_VERSIONS, "aggregate_versions_pk", AggregateVersions.AGGREGATE_VERSIONS.AGGREGATE_ROOT_TYPE, AggregateVersions.AGGREGATE_VERSIONS.AGGREGATE_ROOT_ID)
    val EVENTS_JOURNAL_PK : UniqueKey[EventsJournalRecord] = Internal.createUniqueKey(EventsJournal.EVENTS_JOURNAL, "events_journal_pk", EventsJournal.EVENTS_JOURNAL.EVENT_OFFSET)
    val MEMBERSHIP_MEMBERS_PKEY : UniqueKey[MembershipMembersRecord] = Internal.createUniqueKey(MembershipMembers.MEMBERSHIP_MEMBERS, "membership_members_pkey", MembershipMembers.MEMBERSHIP_MEMBERS.ID)
    val MEMBERSHIP_ORGANIZATIONS_PKEY : UniqueKey[MembershipOrganizationsRecord] = Internal.createUniqueKey(MembershipOrganizations.MEMBERSHIP_ORGANIZATIONS, "membership_organizations_pkey", MembershipOrganizations.MEMBERSHIP_ORGANIZATIONS.ID)
    val PLAY_EVOLUTIONS_PKEY : UniqueKey[PlayEvolutionsRecord] = Internal.createUniqueKey(PlayEvolutions.PLAY_EVOLUTIONS, "play_evolutions_pkey", PlayEvolutions.PLAY_EVOLUTIONS.ID)
  }
}
