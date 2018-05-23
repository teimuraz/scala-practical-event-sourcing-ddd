package backend.membership.infrastructure

import backend.common.{Email, Owner}
import backend.membership.domain.{Member, MemberId, MemberName, MemberRepository}
import javax.inject.{Inject, Singleton}
import library.jooq.{JooqRepositorySupport, TransactionManager}
import org.joda.time.DateTime
import scala.concurrent.Await
import scala.concurrent.duration._
import backend.jooq.generated.Sequences.MEMBERSHIP_MEMBERS_SEQ


@Singleton
class Seeder @Inject()(val transactionManager: TransactionManager, val memberRepository: MemberRepository) extends JooqRepositorySupport {

  /// Member

  val existingMemberOpt = Await.result(memberRepository.findById(MemberId(1)), 10 seconds)

  if (existingMemberOpt.isEmpty) {
      val member: Member = Member.create(MemberId(1), MemberName("teimuraz"), Email("teimuraz.kantaria@gmail.com"), Owner, DateTime.now())

      Await.result(transactionManager.execute { implicit rc =>
        memberRepository.save(member)
        rc.dsl.nextval(MEMBERSHIP_MEMBERS_SEQ)
    }, 100 seconds)
  }
}


