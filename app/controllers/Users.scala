package controllers

import play.api._
import play.api.mvc._
import models.User
import play.api.data._
import play.api.data.Forms._
import anorm._
import anorm.SqlParser._
import services.RouletteService


object UsersController extends Controller {



  def addUser = Action {
    Ok(views.html.addUser())
  }

  def addUserPost = Action { implicit request =>

    val EntryForm = Form(
      mapping(
        "id" -> ignored(NotAssigned: Pk[Int]),
        "email" -> email
      )(User.apply)(User.unapply))

    EntryForm.bindFromRequest.fold(
      formWithErrors => {
        Redirect("/")
      },
      user => {
        User.save(user)
        Redirect("/")
      })
  }

}
