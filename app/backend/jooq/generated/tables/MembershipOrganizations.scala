/*
 * This file is generated by jOOQ.
*/
package backend.jooq.generated.tables


import backend.jooq.generated.Indexes
import backend.jooq.generated.Keys
import backend.jooq.generated.Public
import backend.jooq.generated.tables.records.MembershipOrganizationsRecord

import java.lang.Class
import java.lang.Integer
import java.lang.Long
import java.lang.String
import java.util.Arrays
import java.util.List

import javax.annotation.Generated

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


object MembershipOrganizations {

  /**
   * The reference instance of <code>public.membership_organizations</code>
   */
  val MEMBERSHIP_ORGANIZATIONS = new MembershipOrganizations
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
class MembershipOrganizations(alias : Name, aliased : Table[MembershipOrganizationsRecord], parameters : Array[ Field[_] ]) extends TableImpl[MembershipOrganizationsRecord](alias, Public.PUBLIC, aliased, parameters, "") {

  /**
   * The class holding records for this type
   */
  override def getRecordType : Class[MembershipOrganizationsRecord] = {
    classOf[MembershipOrganizationsRecord]
  }

  /**
   * The column <code>public.membership_organizations.id</code>.
   */
  val ID : TableField[MembershipOrganizationsRecord, Long] = createField("id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), "")

  /**
   * The column <code>public.membership_organizations.name</code>.
   */
  val NAME : TableField[MembershipOrganizationsRecord, String] = createField("name", org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false), "")

  /**
   * The column <code>public.membership_organizations.owners_count</code>.
   */
  val OWNERS_COUNT : TableField[MembershipOrganizationsRecord, Integer] = createField("owners_count", org.jooq.impl.SQLDataType.INTEGER.nullable(false), "")

  /**
   * Create a <code>public.membership_organizations</code> table reference
   */
  def this() = {
    this(DSL.name("membership_organizations"), null, null)
  }

  /**
   * Create an aliased <code>public.membership_organizations</code> table reference
   */
  def this(alias : String) = {
    this(DSL.name(alias), backend.jooq.generated.tables.MembershipOrganizations.MEMBERSHIP_ORGANIZATIONS, null)
  }

  /**
   * Create an aliased <code>public.membership_organizations</code> table reference
   */
  def this(alias : Name) = {
    this(alias, backend.jooq.generated.tables.MembershipOrganizations.MEMBERSHIP_ORGANIZATIONS, null)
  }

  private def this(alias : Name, aliased : Table[MembershipOrganizationsRecord]) = {
    this(alias, aliased, null)
  }

  override def getSchema : Schema = Public.PUBLIC

  override def getIndexes : List[ Index ] = {
    return Arrays.asList[ Index ](Indexes.MEMBERSHIP_ORGANIZATIONS_PKEY)
  }

  override def getPrimaryKey : UniqueKey[MembershipOrganizationsRecord] = {
    Keys.MEMBERSHIP_ORGANIZATIONS_PKEY
  }

  override def getKeys : List[ UniqueKey[MembershipOrganizationsRecord] ] = {
    return Arrays.asList[ UniqueKey[MembershipOrganizationsRecord] ](Keys.MEMBERSHIP_ORGANIZATIONS_PKEY)
  }

  override def as(alias : String) : MembershipOrganizations = {
    new MembershipOrganizations(DSL.name(alias), this)
  }

  override def as(alias : Name) : MembershipOrganizations = {
    new MembershipOrganizations(alias, this)
  }

  /**
   * Rename this table
   */
  override def rename(name : String) : MembershipOrganizations = {
    new MembershipOrganizations(DSL.name(name), null)
  }

  /**
   * Rename this table
   */
  override def rename(name : Name) : MembershipOrganizations = {
    new MembershipOrganizations(name, null)
  }
}
