package library.repository

import play.api.Configuration

case class PagingConfig(itemsPerPage: Int)

trait PagingConfigProvider {
  def config: Configuration
  implicit def pagingConfig: PagingConfig = PagingConfig(
    itemsPerPage = config.getOptional[Int]("paging.itemsPerPage").getOrElse(20)
  )
}

