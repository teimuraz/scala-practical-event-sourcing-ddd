package library.error

class BaseException(message: String, code: Option[ErrorCode] = None, cause: Option[Throwable] = None) extends RuntimeException(message, cause.orNull)
class InternalErrorException(message: String, cause: Option[Throwable] = None) extends BaseException(message, cause = cause) {
  def this(cause: Throwable) {
    this(cause.getMessage, Some(cause))
  }
}

class RethrownFromTrackedStackException(cause: Throwable) extends BaseException(cause.getMessage, None, Some(cause))

class UnauthorizedException(message: String) extends BaseException(message)
class ForbiddenException(message: String) extends BaseException(message)
class ValidationException(message: String, code: Option[ErrorCode] = None, cause: Option[Throwable] = None) extends BaseException(message, code, cause)
class NotFoundException(message: String) extends BaseException(message)
class ConfigurationException(message: String) extends InternalErrorException(message)