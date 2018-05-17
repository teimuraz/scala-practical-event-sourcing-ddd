/*
 * This file is generated by jOOQ.
*/
package backend.jooq.generated.tables


import backend.jooq.generated.Indexes
import backend.jooq.generated.Keys
import backend.jooq.generated.Public
import backend.jooq.generated.tables.records.AmUsersRecord

import java.lang.Class
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


object AmUsers {

  /**
   * The reference instance of <code>public.am_users</code>
   */
  val AM_USERS = new AmUsers
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
class AmUsers(alias : Name, aliased : Table[AmUsersRecord], parameters : Array[ Field[_] ]) extends TableImpl[AmUsersRecord](alias, Public.PUBLIC, aliased, parameters, "") {

  /**
   * The class holding records for this type
   */
  override def getRecordType : Class[AmUsersRecord] = {
    classOf[AmUsersRecord]
  }

  /**
   * The column <code>public.am_users.id</code>.
   */
  val ID : TableField[AmUsersRecord, Long] = createField("id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), "")

  /**
   * The column <code>public.am_users.email</code>.
   */
  val EMAIL : TableField[AmUsersRecord, String] = createField("email", org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false), "")

  /**
   * The column <code>public.am_users.username</code>.
   */
  val USERNAME : TableField[AmUsersRecord, String] = createField("username", org.jooq.impl.SQLDataType.VARCHAR(100).nullable(false), "")

  /**
   * The column <code>public.am_users.password</code>.
   */
  val PASSWORD : TableField[AmUsersRecord, String] = createField("password", org.jooq.impl.SQLDataType.VARCHAR(100).nullable(false), "")

  /**
   * The column <code>public.am_users.salt</code>.
   */
  val SALT : TableField[AmUsersRecord, String] = createField("salt", org.jooq.impl.SQLDataType.VARCHAR(100).nullable(false), "")

  /**
   * The column <code>public.am_users.first_name</code>.
   */
  val FIRST_NAME : TableField[AmUsersRecord, String] = createField("first_name", org.jooq.impl.SQLDataType.VARCHAR(255), "")

  /**
   * The column <code>public.am_users.last_name</code>.
   */
  val LAST_NAME : TableField[AmUsersRecord, String] = createField("last_name", org.jooq.impl.SQLDataType.VARCHAR(255), "")

  /**
   * The column <code>public.am_users.created_at</code>.
   */
  val CREATED_AT : TableField[AmUsersRecord, DateTime] = createField("created_at", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false), "", new JodaDateTimeConverter())

  /**
   * Create a <code>public.am_users</code> table reference
   */
  def this() = {
    this(DSL.name("am_users"), null, null)
  }

  /**
   * Create an aliased <code>public.am_users</code> table reference
   */
  def this(alias : String) = {
    this(DSL.name(alias), backend.jooq.generated.tables.AmUsers.AM_USERS, null)
  }

  /**
   * Create an aliased <code>public.am_users</code> table reference
   */
  def this(alias : Name) = {
    this(alias, backend.jooq.generated.tables.AmUsers.AM_USERS, null)
  }

  private def this(alias : Name, aliased : Table[AmUsersRecord]) = {
    this(alias, aliased, null)
  }

  override def getSchema : Schema = Public.PUBLIC

  override def getIndexes : List[ Index ] = {
    return Arrays.asList[ Index ](Indexes.AM_USERS_EMAIL_IDX, Indexes.AM_USERS_USERNAME_IDX, Indexes.USERS_EMAIL_KEY, Indexes.USERS_PKEY, Indexes.USERS_USERNAME_KEY)
  }

  override def getPrimaryKey : UniqueKey[AmUsersRecord] = {
    Keys.USERS_PKEY
  }

  override def getKeys : List[ UniqueKey[AmUsersRecord] ] = {
    return Arrays.asList[ UniqueKey[AmUsersRecord] ](Keys.USERS_PKEY, Keys.USERS_EMAIL_KEY, Keys.USERS_USERNAME_KEY)
  }

  override def as(alias : String) : AmUsers = {
    new AmUsers(DSL.name(alias), this)
  }

  override def as(alias : Name) : AmUsers = {
    new AmUsers(alias, this)
  }

  /**
   * Rename this table
   */
  override def rename(name : String) : AmUsers = {
    new AmUsers(DSL.name(name), null)
  }

  /**
   * Rename this table
   */
  override def rename(name : Name) : AmUsers = {
    new AmUsers(name, null)
  }
}
