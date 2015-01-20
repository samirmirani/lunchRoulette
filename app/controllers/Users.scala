package controllers

import play.api._
import play.api.mvc._
import models.User
import play.api.data._
import play.api.data.Forms._
import anorm._
import anorm.SqlParser._
import services.RouletteService
import play.api.data._
import play.api.data.Forms._


object UsersController extends Controller {



  def addUser = Action {
    Ok(views.html.addUser(""))
  }

  def addUserPost = Action { implicit request =>

     
    val EntryForm = Form(
      mapping(
        "id" -> ignored(NotAssigned: Pk[Int]),
        "email" -> nonEmptyText.verifying("Email must be in correct format" , emailPost => emailPost.matches("[a-zA-Z0-9\\.]*@creditkarma.com") )
      )(User.apply)(User.unapply))

    EntryForm.bindFromRequest.fold(
      formWithErrors => {
        Ok(views.html.index("Opps...this is embarssing...something went wrong"))
      },
      user => {
        User.save(user)
        Ok(views.html.index("Thanks for entering the roulette, you'll be getting a confirmation e-mail shortly!"))
      })
  }

}
