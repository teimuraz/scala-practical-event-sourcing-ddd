package library.eventsourcing

import library.error.InternalErrorException

class ConcurrencyException(message: String) extends InternalErrorException(message)
