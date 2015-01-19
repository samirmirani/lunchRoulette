package services

import models.User

import scala.util.Random
import play.cache.Cache
import services.MailService
import org.apache.commons.mail._
import play.api.libs.mailer._

object MailService {

  val from: String = "example@example.com"
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
    val email = new SimpleEmail();
    email.setHostName(hostname)
    email.setFrom(from)
    email.addTo(recpient)
    email.setSubject("subject")
    email.setMsg(message)
    Mail.send(email)

    true
  }
}

object RouletteService {


  /**
   * Returns users paired with everyone else.
   * @todo fix the odd one out....if we can't pair someone with someone else.
   * @todo fairly sure this can be done a whole lot better.
   *
   * @param users
   * @return
   */
  def doPairing(users: List[User]):  List[(User, User)] = {
    //remove all paied users.
    User.truncatePairs

    //randomize this list.
    val shuffledUsers : List[User] = Random.shuffle(users)


    val halfOfListCount = shuffledUsers.size / 2;

    val groupOne = shuffledUsers.slice(0, halfOfListCount)
    val groupTwo = shuffledUsers.slice(halfOfListCount, shuffledUsers.size)


    //this will pair everyone together.
    val pairUsers = groupOne zip groupTwo

    for ((pair) <- pairUsers) {
      User.savePair(pair)
    }

    System.out.println("Paired users");
    
    pairUsers
  }

  def pairingJob(): Unit = {
    val pairedUsers = RouletteService.doPairing(User.list);
    

    sendMailToPairs(pairedUsers)
  }

  /**
   * This will send an e-mail to all mathced pairs.*
   *
   * @param pairs
   * @return
   */
  private def sendMailToPairs(pairs: List[(User,User)]) : scala.collection.parallel.immutable.ParSeq[Boolean] = {
    def processMail(pair: (User,User)) : Boolean = {
      var recpient = pair._1.email
      var message  = "You have been paired with " + pair._2.email 
      var subject  = "Your pairing for lunch roulette"

      MailService.sendMail(recpient, subject, message)

      recpient = pair._2.email
      message  = "You have been paired with " + pair._1.email
      subject  = "Your pairing for lunch roulette"

      MailService.sendMail(recpient, subject, message)
      
      true
    }
    
    //Parellel process sending all these emails out.
    val result =  pairs.par.map(pair => processMail(pair))



    result
  }
}