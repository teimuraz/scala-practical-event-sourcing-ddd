package library.ddd.domainrule

import library.error.{ForbiddenException, ValidationException}

import scala.util.Try

sealed trait DomainRuleResult

case object Passed extends DomainRuleResult
case class FailedPermission(message: Option[String] = None) extends DomainRuleResult
case class FailedValidation(message: Option[String] = None) extends DomainRuleResult

trait DomainRuleData


trait DomainRule[D <: DomainRuleData] {

  def check(data: D): Try[Unit] = Try({
    resolve(data) match {
      case Passed => ()
      case FailedPermission(msg) => throw new ForbiddenException(msg.getOrElse("Your are not allowed to perform this action."))
      case FailedValidation(msg) => throw new ValidationException(msg.getOrElse("Validation failed."))
    }
  })

  protected def resolve(data: D): DomainRuleResult
}


