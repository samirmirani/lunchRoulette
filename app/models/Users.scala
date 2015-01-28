package models

import anorm._
import anorm.SqlParser._
import play.api.db.DB
import play.api.Play.current
import services._
import play.libs.Akka

import akka.actor._
import scala.concurrent.duration._
import play.api.libs.concurrent.Execution.Implicits._

case class User(id: Pk[Int], email: String)

object User {
  /**
   * Save a user to the DB.
   *
   * @param user
   */
  def save(user: User) {
    DB.withConnection { implicit connection =>
      SQL("""
            INSERT INTO users.users(email)
            VALUES({email})
          """).on(
          'email -> user.email
        ).executeUpdate
    }
    

    Akka.system.scheduler.scheduleOnce(0 seconds,  new Runnable {
      def run() {
       // MailService.sendMail(user.email, "You have entered the roulette", "You will be recieveing your lunch partner @ 10:30 am PST")
      }
    })

  }

  /**
   * This will go through each row of a result and turn the result of a row into the user object.
   */
  private val userParser: RowParser[User] = {
      get[Pk[Int]]("id") ~
      get[String]("email")  map {
        case id  ~ email => User(id, email)
      }
  }

  /**
   * Will get all the users from the DB.
   *
   * @return List[User]
   */
    def list = {
      DB.withConnection { implicit connection =>
        SQL("SELECT * from users.users").as(userParser *)
      }
    }

  def savePair(pair: (User,User)) = {
    System.out.println(pair)
    DB.withConnection { implicit connection =>
      SQL("""
            INSERT INTO users.pairs(userida, useridb)
            VALUES({idA}, {idB})
          """).on(
          'idA -> pair._1.id.get,
          'idB -> pair._2.id.get
        ).executeUpdate
    }
  }

  def truncatePairs() = {
    DB.withConnection { implicit c =>
      val result: Boolean = SQL("TRUNCATE users.pairs").execute()
    }
  }

  def truncateUsers() = {
 //   DB.withConnection { implicit c =>
 //     val result: Boolean = SQL("TRUNCATE users.users").execute()
  //  }
  }

  def listPairs() = {
    DB.withConnection { implicit connection =>
      SQL("SELECT usera.email AS email_a, usera.id AS id_a , " +
        " userb.email AS email_b, userb.id AS id_b  " +
        "FROM users.pairs pair, users.users  usera , users.users userb " +
        "WHERE pair.userida = usera.id AND pair.useridb = userb.id").as(pairParser *)
    }
  }

  /**
   * This will go through each row of a result and turn the result of a row into the user object.
   */
  private val pairParser: RowParser[(User,User)] = {
      get[Pk[Int]]("id_a") ~
      get[String]("email_a") ~
      get[Pk[Int]]("id_b") ~
      get[String]("email_b") map {
      case id_a ~ email_a  ~ id_b ~ email_b  => (User(id_a, email_a), User(id_b, email_b) )
    }
  }

}