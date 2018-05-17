package backend.membership.infrastructure

import backend.common.{Email, Owner}
import backend.membership.domain.{Member, MemberId, MemberName, MemberRepository}
import javax.inject.{Inject, Singleton}
import library.jooq.{JooqRepositorySupport, TransactionManager}
import org.joda.time.DateTime

import scala.concurrent.Await
import scala.concurrent.duration._

//@Singleton
//class Seeder @Inject()(val transactionManager: TransactionManager, val memberRepository: MemberRepository) extends JooqRepositorySupport {
//
//  val member = Member.create(
//    MemberId(1),
//    MemberName("teimuraz"),
//    Email("teimuraz.kantaria@gmail.com"),
//    Owner,
//    DateTime.now()
//  )
//
//  Await.result(transactionManager.execute { implicit rc =>
//    memberRepository.save(member)
//  }, 100 seconds)
//
//}

object Abc {
  def aaa = 10
}

@Singleton
class Seeder @Inject()(val transactionManager: TransactionManager, val memberRepository: MemberRepository) extends JooqRepositorySupport {

    val member = Member.create2

}


