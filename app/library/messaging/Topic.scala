package library.messaging

import org.apache.commons.lang3.mutable.Mutable

import scala.collection.mutable

case object NoData

class Topic[E, D] {
  private val subscribers: mutable.MutableList[Subscriber[E, D]] = mutable.MutableList.empty

  implicit def noData: NoData.type = NoData

  def attachSubscriber(subscriber: Subscriber[E, D]): Unit = {
    synchronized {
      subscribers += subscriber
    }
  }

  def publish(message: E)(implicit additionalData: D): Unit = {
    subscribers.foreach(_.handle(message))
  }
}
