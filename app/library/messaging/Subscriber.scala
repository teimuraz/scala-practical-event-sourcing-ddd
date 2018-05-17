package library.messaging

trait Subscriber[E, D] {

  implicit def noData: NoData.type = NoData

  def topic: Topic[E, D]

  topic.attachSubscriber(this)

  /**
   * Handle event in the same thread it was published
   * @param message
   * @param additionalData
   */
  def handle(message: E)(implicit additionalData: D): Unit
}
