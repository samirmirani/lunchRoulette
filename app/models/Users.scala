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
        MailService.sendMail(user.email, "You have entered the roulette", "You will be recieveing your lunch partner @ 10:30 am PST")
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



  def truncateUsers() = {
    DB.withConnection { implicit c =>
      val result: Boolean = SQL("TRUNCATE users.users").execute()
    }
  }



}