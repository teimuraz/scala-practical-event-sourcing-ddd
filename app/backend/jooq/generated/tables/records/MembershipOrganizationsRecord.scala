/*
 * This file is generated by jOOQ.
*/
package backend.jooq.generated.tables.records


import backend.jooq.generated.tables.MembershipOrganizations

import java.lang.Integer
import java.lang.Long
import java.lang.String

import javax.annotation.Generated

import org.jooq.Field
import org.jooq.Record1
import org.jooq.Record3
import org.jooq.Row3
import org.jooq.impl.UpdatableRecordImpl

import scala.Array


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
class MembershipOrganizationsRecord extends UpdatableRecordImpl[MembershipOrganizationsRecord](MembershipOrganizations.MEMBERSHIP_ORGANIZATIONS) with Record3[Long, String, Integer] {

  /**
   * Setter for <code>public.membership_organizations.id</code>.
   */
  def setId(value : Long) : Unit = {
    set(0, value)
  }

  /**
   * Getter for <code>public.membership_organizations.id</code>.
   */
  def getId : Long = {
    val r = get(0)
    if (r == null) null else r.asInstanceOf[Long]
  }

  /**
   * Setter for <code>public.membership_organizations.name</code>.
   */
  def setName(value : String) : Unit = {
    set(1, value)
  }

  /**
   * Getter for <code>public.membership_organizations.name</code>.
   */
  def getName : String = {
    val r = get(1)
    if (r == null) null else r.asInstanceOf[String]
  }

  /**
   * Setter for <code>public.membership_organizations.owners_count</code>.
   */
  def setOwnersCount(value : Integer) : Unit = {
    set(2, value)
  }

  /**
   * Getter for <code>public.membership_organizations.owners_count</code>.
   */
  def getOwnersCount : Integer = {
    val r = get(2)
    if (r == null) null else r.asInstanceOf[Integer]
  }

  // -------------------------------------------------------------------------
  // Primary key information
  // -------------------------------------------------------------------------
  override def key : Record1[Long] = {
    return super.key.asInstanceOf[ Record1[Long] ]
  }

  // -------------------------------------------------------------------------
  // Record3 type implementation
  // -------------------------------------------------------------------------

  override def fieldsRow : Row3[Long, String, Integer] = {
    super.fieldsRow.asInstanceOf[ Row3[Long, String, Integer] ]
  }

  override def valuesRow : Row3[Long, String, Integer] = {
    super.valuesRow.asInstanceOf[ Row3[Long, String, Integer] ]
  }
  override def field1 : Field[Long] = MembershipOrganizations.MEMBERSHIP_ORGANIZATIONS.ID
  override def field2 : Field[String] = MembershipOrganizations.MEMBERSHIP_ORGANIZATIONS.NAME
  override def field3 : Field[Integer] = MembershipOrganizations.MEMBERSHIP_ORGANIZATIONS.OWNERS_COUNT
  override def component1 : Long = getId
  override def component2 : String = getName
  override def component3 : Integer = getOwnersCount
  override def value1 : Long = getId
  override def value2 : String = getName
  override def value3 : Integer = getOwnersCount

  override def value1(value : Long) : MembershipOrganizationsRecord = {
    setId(value)
    this
  }

  override def value2(value : String) : MembershipOrganizationsRecord = {
    setName(value)
    this
  }

  override def value3(value : Integer) : MembershipOrganizationsRecord = {
    setOwnersCount(value)
    this
  }

  override def values(value1 : Long, value2 : String, value3 : Integer) : MembershipOrganizationsRecord = {
    this.value1(value1)
    this.value2(value2)
    this.value3(value3)
    this
  }

  /**
   * Create a detached, initialised MembershipOrganizationsRecord
   */
  def this(id : Long, name : String, ownersCount : Integer) = {
    this()

    set(0, id)
    set(1, name)
    set(2, ownersCount)
  }
}
