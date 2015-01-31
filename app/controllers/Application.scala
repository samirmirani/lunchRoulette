package controllers

import play.api._
import play.api.mvc._
import services._

object Application extends Controller {

  def index = Action {
    //this is a debug tool to test grouping
    //RouletteService.doGrouping
    Ok(views.html.index(""))
  }

}