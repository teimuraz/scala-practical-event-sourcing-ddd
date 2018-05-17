package library.jooq

import java.sql.Connection

import akka.actor.ActorSystem
import javax.inject.{Inject, Singleton}
import library.repository.RepComponents
import org.jooq.impl.DSL
import org.jooq.{DSLContext, SQLDialect}
import play.api.db.Database

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TransactionManager @Inject()
    (db: Database, system: ActorSystem) {

  val databaseContext: ExecutionContext = system.dispatchers.lookup("contexts.database")


  def withTransaction[A](block: DSLContext => A): Future[A] = Future {
    db.withTransaction { connection: Connection =>
      val sql: DSLContext = DSL.using(connection, SQLDialect.POSTGRES_9_4)
      block(sql)
    }
  }(databaseContext)

  def execute[T](block: RepComponents => T): Future[T] =Future {
    db.withTransaction { connection: Connection =>
      block(RepComponents(connection))
    }
  }(databaseContext)
}
