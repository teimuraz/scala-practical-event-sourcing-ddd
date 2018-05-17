package library.error

class BaseException(message: String, code: Option[ErrorCode] = None, cause: Option[Throwable] = None) extends RuntimeException(message, cause.orNull)
class InternalErrorException(message: String, cause: Option[Throwable] = None) extends BaseException(message, cause = cause) {
  def this(cause: Throwable) {
    this(cause.getMessage, Some(cause))
  }
}

class RethrownFromTrackedStackException(cause: Throwable) extends BaseException(cause.getMessage, None, Some(cause))

class UserException(message: String) extends BaseException(message)
class UnauthorizedException(message: String) extends UserException(message)
class ForbiddenException(message: String) extends UserException(message)
class ValidationException(message: String) extends UserException(message)
class NotFoundException(message: String) extends UserException(message)
class ConfigurationException(message: String) extends InternalErrorException(message)