package controllers

import backend.auth.AuthContext
import backend.common.Owner
import backend.membership.api.{ChangeMemberEmailReq, CreateNewMemberReq, MembershipService}
import javax.inject.{Inject, Singleton}
import library.error.{UserException, ValidationException}
import play.api.data.Form
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints.nonEmpty

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class MembershipController @Inject()
    (membershipService: MembershipService, messagesAction: MessagesActionBuilder, cc: ControllerComponents)
    (implicit ec: ExecutionContext, assetsFinder: AssetsFinder)
  extends AbstractController(cc) {

  // Hardcode logged in member
  implicit val authContext: AuthContext = AuthContext(1, Owner)

  def index: Action[AnyContent] = Action.async {
    membershipService.getMembers.map { members =>
      Ok(views.html.membership.index(members))
    }
  }

  val createNewMemberForm = Form(
    mapping(
      "name" -> nonEmptyText,
      "email" -> email.verifying(nonEmpty),
    )(CreateNewMemberReq.apply)(CreateNewMemberReq.unapply)
  )

  val changeMemberEmailForm = Form(
    mapping(
      "id" -> longNumber,
      "email" -> email.verifying(nonEmpty)
    )(ChangeMemberEmailReq.apply)(ChangeMemberEmailReq.unapply)
  )

  def createNewMember: Action[AnyContent] = messagesAction.async { implicit req =>
    Future.successful {
      Ok(views.html.membership.createNewMember(createNewMemberForm))
    }
  }

  def doCreateNewMember: Action[AnyContent] = messagesAction.async { implicit req =>
    createNewMemberForm.bindFromRequest.fold(
      formWithErrors => {
        Future.successful {
          BadRequest(views.html.membership.createNewMember(formWithErrors))
        }
      },
      createNewMemberReq => {
        membershipService.createNewMember(createNewMemberReq).map( member =>
          Redirect(routes.MembershipController.index())
        ).recover {
          case e: UserException =>
            val form = createNewMemberForm.fill(createNewMemberReq).withGlobalError(e.getMessage)
            BadRequest(views.html.membership.createNewMember(form))
        }
      }
    )
  }

  def view(memberId: Long): Action[AnyContent] = messagesAction.async { implicit req =>
    membershipService.getMember(memberId).map(member => Ok(views.html.membership.viewMember(member)))
  }

  def changeEmail(memberId: Long): Action[AnyContent] = messagesAction.async { implicit req =>
    membershipService.getMember(memberId).map { member =>
      val req = ChangeMemberEmailReq(member.id, "")
      Ok(views.html.membership.changeMemberEmail(changeMemberEmailForm.fill(req), member))
    }
  }
}
