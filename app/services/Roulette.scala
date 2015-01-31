package services

import models._
import scala.util.Random
import services.MailService
import org.apache.commons.mail._
import com.typesafe.plugin._
import play.api.Play.current
import play.api.Play


/**
 * This is how the application will fire off grouping all users into random groups.
 */
object RouletteService {
  def doGrouping(): Unit = {
    //Get all the users from the DB.
    val users : List[User] = User.list
    if (users.size > 1 ) {
      val groups  : Groups = new Groups(users)
      //Notify each user of who they are grouped with. Do this in parallel (this could be a decent amount of emails)
      groups.groups.par.map(_.notifyMembers())
    }
  }
}

/**
 * Wrapper for sending mail
 */
object MailService {

  /**
   * Sends an e-mail out
   * *
   * @param recpient
   * @param subject
   * @param message
   * @return
   */
  def sendMail(recpient: String , subject: String,  message: String): Boolean = {
    val mail = use[MailerPlugin].email
    mail.setFrom(
      Play.current.configuration.getString("smtp.user") match {
      case Some(value) => value
      case None => throw new Exception("smtp.user not set")
    })
    mail.setRecipient(recpient)
    mail.setSubject(subject)

    //if the mail service is enabled. Send message out.
    if (enabled()) {
      mail.send(message)
    }
    true
  }

  /**
   * Checks to make sure that the mail service is enabled.
   * @return
   */
  def enabled() : Boolean = {
    val enabledConfig : String = Play.current.configuration.getString("smtp.enabled") match {
      case Some(value) => value
      case None => throw new Exception("mailservice.enabled not set")
    }

    enabledConfig.toBoolean
  }
}

