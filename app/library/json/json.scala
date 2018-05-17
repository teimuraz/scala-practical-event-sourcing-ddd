package library

import play.api.libs.json._
import play.api.libs.json.Reads.filterNot

import scala.reflect.macros.blackbox

case object json {

  /**
   * Implicitly convert JsPath to JsPathHelper to enable additional helper methods
   * @param path
   */
  implicit class JsPathHelper(val path: JsPath) {
    def trimmedString(implicit r: Reads[String]): Reads[String] = Reads.at[String](path).map(_.trim)
  }

  def minLengthTrimmed(m: Int)(implicit reads: Reads[String]): Reads[String] =
    filterNot[String](JsonValidationError("error.minLength", m))(_.trim.length < m)

  def maxLengthTrimmed(m: Int)(implicit reads: Reads[String]): Reads[String] =
    filterNot[String](JsonValidationError("error.minLength", m))(_.trim.length > m)

  /**
   * Construct js object from optional js objects
   */
  def jsObjectFromOpts(objects: Option[JsObject]*): JsObject = {
    objects
    .collect {
      case v: Some[JsObject] => v.get
    }
    .reduce((obj, item) => obj ++ item)
  }

//  def valueClassFormat[T](v: T): Format[T] = macro valueClassFormatImpl
//
//  def valueClassFormatImpl(c: blackbox.Context)(v: c.Expr[T])

}
