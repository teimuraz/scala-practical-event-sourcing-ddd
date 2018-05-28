package backend.membership.api.impl

import backend.common.types.member.MemberRole
import backend.membership.api.MemberDto
import javax.inject.{Inject, Singleton}
import library.jooq.Db
import backend.jooq.generated.Tables.MEMBERSHIP_MEMBERS
import backend.jooq.generated.tables.records.MembershipMembersRecord

import scala.collection.JavaConverters._
import scala.concurrent.Future

@Singleton
class MembershipQueryService @Inject()(db: Db) {

  def findById(id: Long): Future[Option[MemberDto]] = {
    db.query { dsl =>
      val record = dsl
        .selectFrom(MEMBERSHIP_MEMBERS)
        .where(MEMBERSHIP_MEMBERS.ID.eq(id))
        .fetchOneInto(classOf[MembershipMembersRecord])

      recordOptToDto(record)
    }
  }

  def findByEmail(email: String): Future[Option[MemberDto]] = {
    db.query { dsl =>
      val record = dsl
        .selectFrom(MEMBERSHIP_MEMBERS)
        .where(MEMBERSHIP_MEMBERS.EMAIL.eq(email))
        .fetchOneInto(classOf[MembershipMembersRecord])

      recordOptToDto(record)
    }
  }

  def findByName(name: String): Future[Option[MemberDto]] = {
    db.query { dsl =>
      val record = dsl
        .selectFrom(MEMBERSHIP_MEMBERS)
        .where(MEMBERSHIP_MEMBERS.NAME.eq(name))
        .fetchOneInto(classOf[MembershipMembersRecord])

      recordOptToDto(record)
    }
  }

  def findAllMembers: Future[Seq[MemberDto]] = {
    db.query { dsl =>
      dsl
        .selectFrom(MEMBERSHIP_MEMBERS)
        .orderBy(MEMBERSHIP_MEMBERS.BECAME_MEMBER_AT)
        .fetchInto(classOf[MembershipMembersRecord])
        .asScala
        .toList
        .map(recordToDto)
    }
  }

  private def recordToDto(record: MembershipMembersRecord): MemberDto = {
    MemberDto(
      id = record.getId,
      name = record.getName,
      email = record.getEmail,
      role = MemberRole.valueOf(record.getRole),
      becameMemberAt = record.getBecameMemberAt
    )
  }

  private def recordOptToDto(record: MembershipMembersRecord): Option[MemberDto] = {
    if (record == null) {
      None
    } else {
      Some(recordToDto(record))
    }
  }

}
