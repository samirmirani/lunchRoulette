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

  def listPairs() = {
    DB.withConnection { implicit connection =>
      SQL("SELECT usera.name AS name_a, usera.email AS email_a, usera.id AS id_a , usera.active AS active_a , " +
        " userb.name AS name_b, userb.email AS email_b, userb.id AS id_b , userb.active AS active_b  " +
        "FROM users.pairs pair, users.users  usera , users.users userb " +
        "WHERE pair.userida = usera.id AND pair.useridb = userb.id").as(pairParser *)
    }
  }

  /**
   * This will go through each row of a result and turn the result of a row into the user object.
   */
  private val pairParser: RowParser[(User,User)] = {
      get[Pk[Int]]("id_a") ~
      get[String]("name_a") ~
      get[String]("email_a") ~
      get[Boolean]("active_a") ~
      get[Pk[Int]]("id_b") ~
      get[String]("name_b") ~
      get[String]("email_b") ~
      get[Boolean]("active_b") map {
      case id_a ~ name_a ~ email_a ~ active_a ~ id_b ~ name_b ~ email_b ~ active_b => (User(id_a, name_a, email_a, active_a), User(id_b, name_b, email_b, active_b) )
    }
  }

}