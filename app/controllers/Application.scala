package controllers

import play.api._
import play.api.mvc._
import services._

object Application extends Controller {

  def index = Action {
    //this is a debug tool to test grouping
    //RouletteService.doGrouping

    val applicationName : String = Play.current.configuration.getString("application.name") match {
      case Some(value) => value
      case None => throw new Exception("application.name not set")
    }

    Ok(views.html.index(applicationName))
  }

}