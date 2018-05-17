package library.repository

case class PageResult[T](
  items: Seq[T],
  totalPages: Int,
  totalItems: Long,
  currentPage: Int,
  itemsPerPage: Int
)

