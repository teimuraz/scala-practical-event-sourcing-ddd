package controllers

import backend.auth.AuthContext
import backend.common.Owner
import backend.membership.api.{ChangeMemberEmailReq, ChangeMemberNameReq, CreateNewMemberReq, MembershipService}
import javax.inject.{Inject, Singleton}
import library.error.{UserException, ValidationException}
import play.api.data.Form
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints.nonEmpty
import views.html.helper.form

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

  val changeMemberNameForm = Form(
    mapping(
      "id" -> longNumber,
      "name" -> nonEmptyText
    )(ChangeMemberNameReq.apply)(ChangeMemberNameReq.unapply)
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
        membershipService.createNewMember(createNewMemberReq).map{ _ =>
          Redirect(routes.MembershipController.index())
        }.recover {
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

  def changeName(memberId: Long): Action[AnyContent] = messagesAction.async { implicit req =>
    membershipService.getMember(memberId).map { member =>
      val changeMemberNameReq = ChangeMemberNameReq(member.id, member.name)
      Ok(views.html.membership.changeMemberName(changeMemberNameForm.fill(changeMemberNameReq), member))
    }
  }

  def doChangeName(memberId: Long): Action[AnyContent] = messagesAction.async { implicit  req =>
    membershipService.getMember(memberId).flatMap { member =>
      changeMemberNameForm.bindFromRequest.fold(
        formWithErrors => {
            Future.successful(BadRequest(views.html.membership.changeMemberName(formWithErrors, member)))
        },
        changeMemberNameReq => {
          membershipService.changeMemberName(changeMemberNameReq).map { _ =>
            Redirect(routes.MembershipController.view(changeMemberNameReq.id))
          }
          .recover {
            case e =>
              val form = changeMemberNameForm.fill(changeMemberNameReq).withGlobalError(e.getMessage)
              BadRequest(views.html.membership.changeMemberName(form, member))
          }
        }
      )
    }
  }

  def changeEmail(memberId: Long): Action[AnyContent] = messagesAction.async { implicit req: MessagesRequest[AnyContent] =>
    membershipService.getMember(memberId).map { member =>
      val changeMemberEmailReq = ChangeMemberEmailReq(member.id, member.email)
      Ok(views.html.membership.changeMemberEmail(changeMemberEmailForm.fill(changeMemberEmailReq), member))
    }
  }

  def doChangeEmail(memberId: Long): Action[AnyContent] = messagesAction.async { implicit  req =>
    membershipService.getMember(memberId).flatMap { member =>
      changeMemberEmailForm.bindFromRequest.fold(
        formWithErrors => {
          Future.successful(BadRequest(views.html.membership.changeMemberEmail(formWithErrors, member)))
        },
        changeMemberEmailReq => {
          membershipService.changeMemberEmail(changeMemberEmailReq).map { _ =>
            Redirect(routes.MembershipController.view(changeMemberEmailReq.id))
          }
            .recover {
              case e =>
                val form = changeMemberEmailForm.fill(changeMemberEmailReq).withGlobalError(e.getMessage)
                BadRequest(views.html.membership.changeMemberEmail(form, member))
            }
        }
      )
    }
  }

  def makeMemberAnOwner(memberId: Long): Action[AnyContent] = messagesAction.async { implicit req =>
    membershipService.makeMemberAnOwner(memberId).map(_ => Redirect(routes.MembershipController.view(memberId)))
  }

  def makeMemberAStandardMember(memberId: Long): Action[AnyContent] = messagesAction.async { implicit req =>
    membershipService.makeMemberAStandardMember(memberId).map(_ => Redirect(routes.MembershipController.view(memberId)))
  }

  def disconnectMember(memberId: Long): Action[AnyContent] = messagesAction.async { implicit req =>
    membershipService.disconnectMember(memberId).map(_ => Redirect(routes.MembershipController.index()))
  }
}
