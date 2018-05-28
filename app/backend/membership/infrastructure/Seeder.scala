package backend.membership.infrastructure

import backend.common.types._
import backend.common.types.member.{MemberId, MemberName, Owner}
import backend.common.types.organization.{OrganizationId, OrganizationName}
import backend.membership.domain._
import javax.inject.{Inject, Singleton}
import library.jooq.{JooqRepositorySupport, TransactionManager}
import org.joda.time.DateTime

import scala.concurrent.Await
import scala.concurrent.duration._
import backend.jooq.generated.Sequences.MEMBERSHIP_MEMBERS_SEQ
import backend.jooq.generated.Sequences.MEMBERSHIP_ORGANIZATIONS_SEQ


@Singleton
class Seeder @Inject()(
    val transactionManager: TransactionManager,
    val memberRepository: MemberRepository,
    val organizationRepository: OrganizationRepository
) extends JooqRepositorySupport {

  /// Organization

  val existingOrganizationOpt = Await.result(organizationRepository.findById(OrganizationId(1)), 10 seconds)

  if (existingOrganizationOpt.isEmpty) {
    val organization = Organization.create(OrganizationId(1), OrganizationName("Acme"))

    Await.result(transactionManager.execute { implicit rc =>
      organizationRepository.save(organization)
      rc.dsl.nextval(MEMBERSHIP_ORGANIZATIONS_SEQ)
    }, 100 seconds)
  }

  /// Member

  val existingMemberOpt = Await.result(memberRepository.findById(MemberId(1)), 10 seconds)

  if (existingMemberOpt.isEmpty) {
      val member = Member(
        MemberId(1),
        MemberName("teimuraz"),
        Email("teimuraz.kantaria@gmail.com"),
        Owner,
        OrganizationId(1),
        DateTime.now()
      )

      Await.result(transactionManager.execute { implicit rc =>
        memberRepository.save(member)
        rc.dsl.nextval(MEMBERSHIP_MEMBERS_SEQ)
    }, 100 seconds)
  }



}


