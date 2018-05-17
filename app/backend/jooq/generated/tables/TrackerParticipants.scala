/*
 * This file is generated by jOOQ.
*/
package backend.jooq.generated.tables


import backend.jooq.generated.Indexes
import backend.jooq.generated.Keys
import backend.jooq.generated.Public
import backend.jooq.generated.tables.records.TrackerParticipantsRecord

import java.lang.Class
import java.lang.Integer
import java.lang.Long
import java.lang.String
import java.util.Arrays
import java.util.List

import javax.annotation.Generated

import library.jooq.JodaDateTimeConverter

import org.joda.time.DateTime
import org.jooq.Field
import org.jooq.Index
import org.jooq.Name
import org.jooq.Schema
import org.jooq.Table
import org.jooq.TableField
import org.jooq.UniqueKey
import org.jooq.impl.DSL
import org.jooq.impl.TableImpl

import scala.Array


object TrackerParticipants {

  /**
   * The reference instance of <code>public.tracker_participants</code>
   */
  val TRACKER_PARTICIPANTS = new TrackerParticipants
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
class TrackerParticipants(alias : Name, aliased : Table[TrackerParticipantsRecord], parameters : Array[ Field[_] ]) extends TableImpl[TrackerParticipantsRecord](alias, Public.PUBLIC, aliased, parameters, "") {

  /**
   * The class holding records for this type
   */
  override def getRecordType : Class[TrackerParticipantsRecord] = {
    classOf[TrackerParticipantsRecord]
  }

  /**
   * The column <code>public.tracker_participants.id</code>.
   */
  val ID : TableField[TrackerParticipantsRecord, Long] = createField("id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), "")

  /**
   * The column <code>public.tracker_participants.user_id</code>.
   */
  val USER_ID : TableField[TrackerParticipantsRecord, Long] = createField("user_id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), "")

  /**
   * The column <code>public.tracker_participants.organization_id</code>.
   */
  val ORGANIZATION_ID : TableField[TrackerParticipantsRecord, Long] = createField("organization_id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), "")

  /**
   * The column <code>public.tracker_participants.invited_at</code>.
   */
  val INVITED_AT : TableField[TrackerParticipantsRecord, DateTime] = createField("invited_at", org.jooq.impl.SQLDataType.TIMESTAMP, "", new JodaDateTimeConverter())

  /**
   * The column <code>public.tracker_participants.invited_by_participant_id</code>.
   */
  val INVITED_BY_PARTICIPANT_ID : TableField[TrackerParticipantsRecord, Long] = createField("invited_by_participant_id", org.jooq.impl.SQLDataType.BIGINT, "")

  /**
   * The column <code>public.tracker_participants.became_member_at</code>.
   */
  val BECAME_MEMBER_AT : TableField[TrackerParticipantsRecord, DateTime] = createField("became_member_at", org.jooq.impl.SQLDataType.TIMESTAMP, "", new JodaDateTimeConverter())

  /**
   * The column <code>public.tracker_participants.role</code>.
   */
  val ROLE : TableField[TrackerParticipantsRecord, Integer] = createField("role", org.jooq.impl.SQLDataType.INTEGER.nullable(false), "")

  /**
   * Create a <code>public.tracker_participants</code> table reference
   */
  def this() = {
    this(DSL.name("tracker_participants"), null, null)
  }

  /**
   * Create an aliased <code>public.tracker_participants</code> table reference
   */
  def this(alias : String) = {
    this(DSL.name(alias), backend.jooq.generated.tables.TrackerParticipants.TRACKER_PARTICIPANTS, null)
  }

  /**
   * Create an aliased <code>public.tracker_participants</code> table reference
   */
  def this(alias : Name) = {
    this(alias, backend.jooq.generated.tables.TrackerParticipants.TRACKER_PARTICIPANTS, null)
  }

  private def this(alias : Name, aliased : Table[TrackerParticipantsRecord]) = {
    this(alias, aliased, null)
  }

  override def getSchema : Schema = Public.PUBLIC

  override def getIndexes : List[ Index ] = {
    return Arrays.asList[ Index ](Indexes.TRACKER_PARTICIPANTS_ORGANIZATION_ID_IDX, Indexes.TRACKER_PARTICIPANTS_PKEY, Indexes.TRACKER_PARTICIPANTS_USER_ID_IDX, Indexes.TRACKER_PARTICIPANTS_USER_ID_ORGANIZATION_ID_KEY)
  }

  override def getPrimaryKey : UniqueKey[TrackerParticipantsRecord] = {
    Keys.TRACKER_PARTICIPANTS_PKEY
  }

  override def getKeys : List[ UniqueKey[TrackerParticipantsRecord] ] = {
    return Arrays.asList[ UniqueKey[TrackerParticipantsRecord] ](Keys.TRACKER_PARTICIPANTS_PKEY, Keys.TRACKER_PARTICIPANTS_USER_ID_ORGANIZATION_ID_KEY)
  }

  override def as(alias : String) : TrackerParticipants = {
    new TrackerParticipants(DSL.name(alias), this)
  }

  override def as(alias : Name) : TrackerParticipants = {
    new TrackerParticipants(alias, this)
  }

  /**
   * Rename this table
   */
  override def rename(name : String) : TrackerParticipants = {
    new TrackerParticipants(DSL.name(name), null)
  }

  /**
   * Rename this table
   */
  override def rename(name : Name) : TrackerParticipants = {
    new TrackerParticipants(name, null)
  }
}
