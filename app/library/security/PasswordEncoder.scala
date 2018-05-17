package library.security

import play.api.libs.json.{JsString, Reads, Writes}
import play.api.libs.functional.syntax._
import library.json._

trait PasswordEncoder {

  def encode(password: String, salt: String): String

  def generateSalt(length: Int = 10): String
}

