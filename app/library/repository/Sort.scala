package library.repository

trait SortOptions[T <: SortOptions[T]]

sealed trait SortDirection
case object Asc extends SortDirection
case object Desc extends SortDirection

