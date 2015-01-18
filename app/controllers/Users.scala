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

  def index = Action {
    val users = User.list
    Ok(views.html.lunchGoers(  users  ))
  }

  def seePair = Action {
    val pairedUsers = RouletteService.getPairs;
    Ok(views.html.seePairs(  pairedUsers  ))
//   val users = User.list
//    Ok(views.html.lunchGoers(  users  ))
  }

  def addUser = Action {
    Ok(views.html.addUser())
  }

  def addUserPost = Action { implicit request =>

    //todo csrf check

    val form = Form(
      mapping(
        "id" -> ignored(NotAssigned: Pk[Int]),
        "name" -> nonEmptyText,
        "email" -> email,
        "isActive" -> ignored(true))(User.apply)(User.unapply))

    form.bindFromRequest.fold(
      formWithErrors => {
        Redirect("/add")
      },
      user => {
        User.save(user)
        Redirect("/lunch-goers")
      })
  }

}
