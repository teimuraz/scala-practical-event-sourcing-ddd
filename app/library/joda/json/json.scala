package library.joda

import org.joda.time.DateTime
import play.api.libs.json._

package object json {
  implicit lazy val jsonDateTimeWrites: Writes[DateTime] = JodaWrites.jodaDateWrites("yyyy-MM-dd HH:mm:ss")
  implicit lazy val jsonDateTimeReads: Reads[DateTime] = JodaReads.jodaDateReads("yyyy-MM-dd HH:mm:ss")
  implicit lazy val jsonDateTimeFormat: Format[DateTime] = Format(jsonDateTimeReads, jsonDateTimeWrites)
}
