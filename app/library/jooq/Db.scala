package library.jooq

import java.sql.Connection
import javax.inject.{Inject, Singleton}

import akka.actor.ActorSystem
import org.jooq.{DSLContext, SQLDialect}
import org.jooq.impl.DSL
import play.api.db.Database
import play.api.inject.ApplicationLifecycle

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class Db @Inject()(val database: Database, system: ActorSystem, lifecycle: ApplicationLifecycle) {

  val databaseContext: ExecutionContext = system.dispatchers.lookup("contexts.database")

  def query[A](block: DSLContext => A): Future[A] = Future {
    database.withConnection { connection: Connection =>
      val dsl = DSL.using(connection, SQLDialect.POSTGRES_9_4)
      block(dsl)
    }
  }(databaseContext)

  def withTransaction[A](block: DSLContext => A): Future[A] = Future {
    database.withTransaction { connection: Connection =>
      val dsl: DSLContext = DSL.using(connection, SQLDialect.POSTGRES_9_4)
      block(dsl)
    }
  }(databaseContext)

  def querySync[A](block: DSLContext => A): A = {
    database.withConnection { connection: Connection =>
      val dsl = DSL.using(connection, SQLDialect.POSTGRES_9_4)
      block(dsl)
    }
  }
}
