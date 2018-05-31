/*
 * This file is generated by jOOQ.
*/
package backend.jooq.generated.tables


import backend.jooq.generated.Indexes
import backend.jooq.generated.Keys
import backend.jooq.generated.Public
import backend.jooq.generated.tables.records.TrackerIssuesRecord

import java.lang.Class
import java.lang.Long
import java.lang.Short
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


object TrackerIssues {

  /**
   * The reference instance of <code>public.tracker_issues</code>
   */
  val TRACKER_ISSUES = new TrackerIssues
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
class TrackerIssues(alias : Name, aliased : Table[TrackerIssuesRecord], parameters : Array[ Field[_] ]) extends TableImpl[TrackerIssuesRecord](alias, Public.PUBLIC, aliased, parameters, "") {

  /**
   * The class holding records for this type
   */
  override def getRecordType : Class[TrackerIssuesRecord] = {
    classOf[TrackerIssuesRecord]
  }

  /**
   * The column <code>public.tracker_issues.id</code>.
   */
  val ID : TableField[TrackerIssuesRecord, Long] = createField("id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), "")

  /**
   * The column <code>public.tracker_issues.title</code>.
   */
  val TITLE : TableField[TrackerIssuesRecord, String] = createField("title", org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false), "")

  /**
   * The column <code>public.tracker_issues.description</code>.
   */
  val DESCRIPTION : TableField[TrackerIssuesRecord, String] = createField("description", org.jooq.impl.SQLDataType.CLOB.nullable(false), "")

  /**
   * The column <code>public.tracker_issues.status</code>.
   */
  val STATUS : TableField[TrackerIssuesRecord, Short] = createField("status", org.jooq.impl.SQLDataType.SMALLINT.nullable(false), "")

  /**
   * The column <code>public.tracker_issues.created_by</code>.
   */
  val CREATED_BY : TableField[TrackerIssuesRecord, Long] = createField("created_by", org.jooq.impl.SQLDataType.BIGINT.nullable(false), "")

  /**
   * The column <code>public.tracker_issues.created_at</code>.
   */
  val CREATED_AT : TableField[TrackerIssuesRecord, DateTime] = createField("created_at", org.jooq.impl.SQLDataType.TIMESTAMP, "", new JodaDateTimeConverter())

  /**
   * Create a <code>public.tracker_issues</code> table reference
   */
  def this() = {
    this(DSL.name("tracker_issues"), null, null)
  }

  /**
   * Create an aliased <code>public.tracker_issues</code> table reference
   */
  def this(alias : String) = {
    this(DSL.name(alias), backend.jooq.generated.tables.TrackerIssues.TRACKER_ISSUES, null)
  }

  /**
   * Create an aliased <code>public.tracker_issues</code> table reference
   */
  def this(alias : Name) = {
    this(alias, backend.jooq.generated.tables.TrackerIssues.TRACKER_ISSUES, null)
  }

  private def this(alias : Name, aliased : Table[TrackerIssuesRecord]) = {
    this(alias, aliased, null)
  }

  override def getSchema : Schema = Public.PUBLIC

  override def getIndexes : List[ Index ] = {
    return Arrays.asList[ Index ](Indexes.TRACKER_ISSUES_CREATED_BY_IDX, Indexes.TRACKER_ISSUES_PKEY, Indexes.TRACKER_ISSUES_STATUS_IDX, Indexes.TRACKER_ISSUES_TITLE_IDX)
  }

  override def getPrimaryKey : UniqueKey[TrackerIssuesRecord] = {
    Keys.TRACKER_ISSUES_PKEY
  }

  override def getKeys : List[ UniqueKey[TrackerIssuesRecord] ] = {
    return Arrays.asList[ UniqueKey[TrackerIssuesRecord] ](Keys.TRACKER_ISSUES_PKEY)
  }

  override def as(alias : String) : TrackerIssues = {
    new TrackerIssues(DSL.name(alias), this)
  }

  override def as(alias : Name) : TrackerIssues = {
    new TrackerIssues(alias, this)
  }

  /**
   * Rename this table
   */
  override def rename(name : String) : TrackerIssues = {
    new TrackerIssues(DSL.name(name), null)
  }

  /**
   * Rename this table
   */
  override def rename(name : Name) : TrackerIssues = {
    new TrackerIssues(name, null)
  }
}