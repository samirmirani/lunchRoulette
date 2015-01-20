package controllers

import play.api._
import play.api.mvc._
import services._

object Application extends Controller {

  def index = Action {
    RouletteService.pairingJob
    Ok(views.html.index(""))
  }

}