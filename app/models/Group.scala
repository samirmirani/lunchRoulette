package models

import scala.collection.mutable.ListBuffer
import util.Random._
import models.User
import services.MailService


class Group(members: List[User]) {

  var _members = members

  /**
   * Sends out an email to user informing them of their pairing.
   * @param userToSend
   */
  private def sendNotificationMailToUser(userToSend: User)  : Unit = {
    val otherMembers = _members.filterNot(x => userToSend == x )

    val subject = "Your lunch roulette group has formed"
    val message = "You have been grouped with the following people: \n" + otherMembers.foldLeft("") ((message : String , userB : User) => message + "\n" + userB.email)

    System.out.println(message)
   // MailService.sendMail(userToSend.email, subject, message)
  }

  def notifyMembers() : Unit = {
    _members.map(user => sendNotificationMailToUser(user))
  }

}

class Groups(users: List[User]) {

  private var _users = users.toBuffer;
  private var _groups : List[Group] = Nil


  def groupSize : Int = 3
  def maxGroupSize: Int = groupSize + (groupSize - 1)
  /**
   * This will define the members of these groups.
   *
   * @param users
   * @return
   */
  def fillGroups() : List[Group] = {
    //This entire section can be done better. More functional, have to revist this.
    var userCount : Int = _users.size
    System.out.println("userCount")
    System.out.println(userCount)
    do {

      var members = if (_users.size <=  maxGroupSize) {
        //if there is a remainder in the group size just return the list. This way groups will maintain a min of at least the grpsize
        val members = _users.toList
        _users = _users.drop(maxGroupSize)
        userCount = userCount - maxGroupSize
        members
      } else {
        //cut the list by the group size
        val members = _users.slice(0, groupSize).toList
        _users = _users.drop(groupSize)
        userCount = userCount - groupSize
        members
      }


      System.out.println("Number of users in this group")
      System.out.println(members.size)

      System.out.println("Members")
      System.out.println(members)
      _groups = _groups:+ new Group(members)
    } while (userCount > 0)

    _groups
  }

  lazy val groups : List[Group] = {
    fillGroups()
  }

  //some debug stuff
  var groupCount : Int = groups.size
  System.out.println("groupCount")
  System.out.println(groupCount)

}