package services

import models.User

import scala.util.Random
import play.cache.Cache
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


  /**
   * Returns users paired with everyone else.
   * @todo fix the odd one out....if we can't pair someone with someone else.
   * @todo fairly sure this can be done a whole lot better.
   *
   * @param users
   * @return
   */
  def doPairing(usersToPair: List[User]):  List[(User, User)] = {

    System.out.println("Number of users to pair")
    System.out.println(usersToPair.size)
    
    //damn we are boned, there is an odd number of users.
    val isOdd : Boolean = usersToPair.size % 2 == 1
    //make a backup of the users to pair.
    val oddUsersToAppend : List[(User, User)] = List((usersToPair(0), usersToPair(1)))
    
    //remove one from the list if its odd. If not, don't do anything. We will append this odd one out to the list later.
    if(isOdd) {
      //make the list even...it'll be must easier this way.
      usersToPair.drop(1)
    }

    
    //randomize this list, cut in into two groups.
    val shuffledUsers : List[User] = Random.shuffle(usersToPair)
    val halfOfListCount : Int  = shuffledUsers.size / 2
    val groupOne  : List[User] = shuffledUsers.slice(0, halfOfListCount)
    val groupTwo : List[User] = shuffledUsers.slice(halfOfListCount, shuffledUsers.size)


    //this will pair everyone together.
    val pairUsers : List[(User,User)] = groupOne zip groupTwo
    
    //sneak the odd one in.
    val pairs : List[(User , User)] = if (isOdd && groupOne.size > 0) {
      pairUsers ++ oddUsersToAppend
    } else {
      pairUsers
    }


    
    //remove all previously paired users.
    User.truncatePairs
    
    for ((pair) <- pairs) {
      //Save Pairs tot he DB
      User.savePair(pair)
    }

    System.out.println("Paired users");
    
    //return.
    pairs
  }

  def pairingJob(): Unit = {
    val users = User.list;
    if (users.size > 1 ) {
      val pairedUsers = RouletteService.doPairing(users)
      sendMailToPairs(pairedUsers)
    }
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