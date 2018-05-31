package controllers

import backend.membership.api.{CreateNewMemberReq, MembershipService}
import backend.tracker.api.{CreateIssueReq, IssueService}
import javax.inject.{Inject, Singleton}
import play.api.data.Form
import play.api.data.Forms.{email, mapping}
import play.api.data.validation.Constraints.nonEmpty
import play.api.mvc.{ControllerComponents, MessagesActionBuilder}
import play.api.data.Forms._

import scala.concurrent.ExecutionContext

@Singleton
class TrackerController @Inject
  (issueService: IssueService, messagesAction: MessagesActionBuilder, cc: ControllerComponents)
  (implicit ec: ExecutionContext, assetsFinder: AssetsFinder)
  extends {


//  val createNewIssueForm = Form(
//    mapping(
//      "title" -> nonEmptyText,
//      "email" -> email.verifying(nonEmpty),
//    )(CreateIssueReq.apply)(CreateIssueReq.unapply)
//  )
//  def createIssue()

}
