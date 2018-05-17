package library.jooq

import java.sql.Connection

import library.repository.RepComponents
import org.jooq.impl.DSL
import org.jooq.{DSLContext, SQLDialect}

trait JooqRepositorySupport {
//  implicit def repComponentsToDsl(repComponents: RepComponents): DSLContext = DSL.using(repComponents.conn, SQLDialect.POSTGRES_9_4)

  implicit class RepComponentsOps(repComponents: RepComponents) {
    def dsl: DSLContext = DSL.using(repComponents.conn, SQLDialect.POSTGRES_9_4)
  }
}
