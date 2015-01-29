package services

import models._
import scala.util.Random
import services.MailService
import org.apache.commons.mail._
import com.typesafe.plugin._
import play.api.Play.current

object MailService {

  val from: String = "cklunchroullete@gmail.com"
  val hostname: String = "smtp.gmail.com"

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
    mail.setFrom(from)
    mail.setRecipient(recpient)
    mail.setSubject(subject)
    mail.send(message)

    true
  }
}

object RouletteService {




  def pairingJob(): Unit = {
     val users = User.list


    if (users.size > 1 ) {
      val groups  : Groups = new Groups(users)

      //Run the send mail on all groups. Each group is parallelized as much as possible.
      groups.groups.par.map(_.notifyMembers())


//      val pairedUsers = RouletteService.doPairing(users)
//      sendMailToPairs(pairedUsers)
    }
  }

}