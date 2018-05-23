import backend.membership.api.{MemberTopic, MembershipService}
import backend.membership.api.impl.{MembershipQueryService, MembershipServiceImpl}
import backend.membership.domain.{MemberDomainEventTopic, MemberRepository}
import backend.membership.infrastructure.{EventSourcedMemberRepository, MembersProjectionBuilder, Seeder}
import com.google.inject.{AbstractModule, TypeLiteral}
import library.jooq.{Db, TransactionManager}
import library.security.{BCryptPasswordEncoder, PasswordEncoder}
import play.api.libs.concurrent.AkkaGuiceSupport

/**
 * This class is a Guice module that tells Guice how to bind several
 * different property.types. This Guice module is created when the Play
 * application starts.

 * Play will automatically use any class called `Module` that is in
 * the root package. You can create modules in other locations by
 * adding `play.modules.enabled` settings to the `application.conf`
 * configuration file.
 */
class Module extends AbstractModule with AkkaGuiceSupport {

  override def configure() = {

    bind(classOf[PasswordEncoder]).to(classOf[BCryptPasswordEncoder])
    bind(classOf[Db])

    bind(classOf[MembershipService]).to(classOf[MembershipServiceImpl])
    bind(classOf[MembersProjectionBuilder]).asEagerSingleton()
//    bind(classOf[MemberDomainEventTopic]).asEagerSingleton()
    bind(classOf[MembershipQueryService])
    bind(classOf[MemberRepository]).to(classOf[EventSourcedMemberRepository])
    bind(classOf[TransactionManager])
    bind(classOf[Seeder]).asEagerSingleton()


  }
}
