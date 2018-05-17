package backend.membership.api.impl

import backend.membership.api.MemberDto
import javax.inject.Singleton

import scala.concurrent.Future

@Singleton
class MembershipQueryService {
  def findByEmail(email: String): Future[Option[MemberDto]] = {
    ???
  }

  def findByName(name: String): Future[Option[MemberDto]] = {
    ???
  }
}
