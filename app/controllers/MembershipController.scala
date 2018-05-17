package controllers

import backend.auth.AuthContext
import backend.membership.api.{CreateNewMemberReq, MembershipService}
import javax.inject.{Inject, Singleton}
import library.error.UserException
import play.api.data.Form
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class MembershipController @Inject()
    (membershipService: MembershipService, messagesAction: MessagesActionBuilder, cc: ControllerComponents)
    (implicit ec: ExecutionContext, assetsFinder: AssetsFinder)
  extends AbstractController(cc) {

  // Hardcode logged in member
  implicit val authContext: AuthContext = AuthContext(100)

  def index: Action[AnyContent] = Action.async {
    membershipService.getMembers.map { members =>
      Ok(views.html.membership.index(members))
    }
  }

  val createNewMemberForm = Form(
    mapping(
      "name" -> nonEmptyText,
      "email" -> email
    )(CreateNewMemberReq.apply)(CreateNewMemberReq.unapply)
  )

  def createNewMember: Action[AnyContent] = messagesAction.async { implicit request: MessagesRequest[AnyContent] =>
    Future.successful {
      Ok(views.html.membership.createNewMember(createNewMemberForm))
    }
  }

  def doCreateNewMember: Action[AnyContent] = messagesAction.async { implicit request: MessagesRequest[AnyContent] =>
    createNewMemberForm.bindFromRequest.fold(
      formWithErrors => {
        Future.successful {
          BadRequest(views.html.membership.createNewMember(formWithErrors))
        }
      },
      createNewMemberReq => {
        membershipService.createNewMember(createNewMemberReq).map( member =>
          Redirect(routes.MembershipController.index())
        )
      }
    )
  }
}
