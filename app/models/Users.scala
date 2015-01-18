package models

import anorm._
import anorm.SqlParser._
import play.api.db.DB
import play.api.Play.current

case class User(id: Pk[Int], name: String, email: String, isActive: Boolean)

object User {
  /**
   * Save a user to the DB.
   *
   * @param user
   */
  def save(user: User) {
    DB.withConnection { implicit connection =>
      SQL("""
            INSERT INTO users.users(name, email, active)
            VALUES({name}, {email}, {active})
          """).on(
          'name -> user.name,
          'email -> user.email,
          'active -> user.isActive
        ).executeUpdate
    }
  }

  /**
   * This will go through each row of a result and turn the result of a row into the user object.
   */
  private val userParser: RowParser[User] = {
      get[Pk[Int]]("id") ~
      get[String]("name") ~
      get[String]("email") ~
      get[Boolean]("active") map {
        case id ~ name ~ email ~ isActive => User(id, name, email, isActive)
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

}