/*
 * This file is generated by jOOQ.
*/
package backend.jooq.generated.tables


import backend.jooq.generated.Indexes
import backend.jooq.generated.Keys
import backend.jooq.generated.Public
import backend.jooq.generated.tables.records.MembershipMembersRecord

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


object MembershipMembers {

  /**
   * The reference instance of <code>public.membership_members</code>
   */
  val MEMBERSHIP_MEMBERS = new MembershipMembers
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
class MembershipMembers(alias : Name, aliased : Table[MembershipMembersRecord], parameters : Array[ Field[_] ]) extends TableImpl[MembershipMembersRecord](alias, Public.PUBLIC, aliased, parameters, "") {

  /**
   * The class holding records for this type
   */
  override def getRecordType : Class[MembershipMembersRecord] = {
    classOf[MembershipMembersRecord]
  }

  /**
   * The column <code>public.membership_members.id</code>.
   */
  val ID : TableField[MembershipMembersRecord, Long] = createField("id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), "")

  /**
   * The column <code>public.membership_members.name</code>.
   */
  val NAME : TableField[MembershipMembersRecord, String] = createField("name", org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false), "")

  /**
   * The column <code>public.membership_members.email</code>.
   */
  val EMAIL : TableField[MembershipMembersRecord, String] = createField("email", org.jooq.impl.SQLDataType.VARCHAR(100).nullable(false), "")

  /**
   * The column <code>public.membership_members.role</code>.
   */
  val ROLE : TableField[MembershipMembersRecord, Integer] = createField("role", org.jooq.impl.SQLDataType.INTEGER.nullable(false), "")

  /**
   * The column <code>public.membership_members.organization_id</code>.
   */
  val ORGANIZATION_ID : TableField[MembershipMembersRecord, Long] = createField("organization_id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), "")

  /**
   * The column <code>public.membership_members.became_member_at</code>.
   */
  val BECAME_MEMBER_AT : TableField[MembershipMembersRecord, DateTime] = createField("became_member_at", org.jooq.impl.SQLDataType.TIMESTAMP, "", new JodaDateTimeConverter())

  /**
   * Create a <code>public.membership_members</code> table reference
   */
  def this() = {
    this(DSL.name("membership_members"), null, null)
  }

  /**
   * Create an aliased <code>public.membership_members</code> table reference
   */
  def this(alias : String) = {
    this(DSL.name(alias), backend.jooq.generated.tables.MembershipMembers.MEMBERSHIP_MEMBERS, null)
  }

  /**
   * Create an aliased <code>public.membership_members</code> table reference
   */
  def this(alias : Name) = {
    this(alias, backend.jooq.generated.tables.MembershipMembers.MEMBERSHIP_MEMBERS, null)
  }

  private def this(alias : Name, aliased : Table[MembershipMembersRecord]) = {
    this(alias, aliased, null)
  }

  override def getSchema : Schema = Public.PUBLIC

  override def getIndexes : List[ Index ] = {
    return Arrays.asList[ Index ](Indexes.MEMBERSHIP_MEMBERS_EMAIL_IDX, Indexes.MEMBERSHIP_MEMBERS_NAME_IDX, Indexes.MEMBERSHIP_MEMBERS_ORGANIZATION_ID_IDX, Indexes.MEMBERSHIP_MEMBERS_PKEY)
  }

  override def getPrimaryKey : UniqueKey[MembershipMembersRecord] = {
    Keys.MEMBERSHIP_MEMBERS_PKEY
  }

  override def getKeys : List[ UniqueKey[MembershipMembersRecord] ] = {
    return Arrays.asList[ UniqueKey[MembershipMembersRecord] ](Keys.MEMBERSHIP_MEMBERS_PKEY)
  }

  override def as(alias : String) : MembershipMembers = {
    new MembershipMembers(DSL.name(alias), this)
  }

  override def as(alias : Name) : MembershipMembers = {
    new MembershipMembers(alias, this)
  }

  /**
   * Rename this table
   */
  override def rename(name : String) : MembershipMembers = {
    new MembershipMembers(DSL.name(name), null)
  }

  /**
   * Rename this table
   */
  override def rename(name : Name) : MembershipMembers = {
    new MembershipMembers(name, null)
  }
}
