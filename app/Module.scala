import backend.membership.api.MembershipService
import backend.membership.api.impl.{MembershipQueryService, MembershipServiceImpl}
import backend.membership.domain.{MemberRepository, OrganizationRepository}
import backend.membership.infrastructure._
import backend.tracker.domain.IssueRepository
import backend.tracker.infrastructure.{EventSourcedIssueRepository, MembershipMemberApiEventsConsumer}
import com.google.inject.{AbstractModule, TypeLiteral}
import library.jooq.{Db, TransactionManager}
import library.messaging.Topic
import library.repository.RepComponents
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

    bind(classOf[TransactionManager])


    // Membership bounded context

    bind(new TypeLiteral[Topic[backend.membership.api.event.MemberEvent, RepComponents]] {}).asEagerSingleton()
    bind(classOf[MembershipService]).to(classOf[MembershipServiceImpl])
    bind(classOf[MembersProjectionBuilder]).asEagerSingleton()
    bind(classOf[MembershipQueryService])
    bind(classOf[backend.membership.domain.MemberRepository]).to(classOf[backend.membership.infrastructure.EventSourcedMemberRepository])


    bind(new TypeLiteral[Topic[backend.membership.api.event.OrganizationEvent, RepComponents]] {}).asEagerSingleton()
    bind(classOf[OrganizationRepository]).to(classOf[EventSourcedOrganizationRepository])
    bind(classOf[OrganizationsProjectionBuilder]).asEagerSingleton()
    bind(classOf[OrganizationOwnersCountUpdater]).asEagerSingleton()



    // Tracker bounded context

    bind(classOf[MembershipMemberApiEventsConsumer]).asEagerSingleton()
    bind(classOf[backend.tracker.domain.MemberRepository]).to(classOf[backend.tracker.infrastructure.EventSourcedMemberRepository])
    bind(classOf[IssueRepository]).to(classOf[EventSourcedIssueRepository])

    // Bind seeders after all bounded contexts, since they should be loaded after all event listeners were bound.

    bind(classOf[Seeder]).asEagerSingleton()


  }
}
