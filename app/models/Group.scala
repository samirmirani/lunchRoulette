package models

import services.MailService

/**
 * This a the single entity class for a group of users.
 *
 *
 * @param members - List of User - The grouped users
 */
class Group(members: List[User]) {

  var _members = members

  /**
   * Sends out an email to user informing them of their pairing.
   * @param userToSend
   */
  private def sendNotificationMailToUser(userToSend: User)  : Unit = {
    val otherMembers = _members.filterNot(x => userToSend == x )

    val subject = "Your lunch roulette group has formed"
    //Message to send to user. Get the other members of the groups email and join a new line seperated list of them
    val message : String = "You have been grouped with the following people: \n" + otherMembers.foldLeft("") (
      //This makes the list of emails seen in the grouping email. Scala...is awesome.
      (message : String , userB : User) => message + "\n" + userB.email
    )

    MailService.sendMail(userToSend.email, subject, message)
  }

  /**
   * This will notify all members in the group via email of whom they are grouped with.....handle with care.
   */
  def notifyMembers() : Unit = {
    _members.map(user => sendNotificationMailToUser(user))
  }

}

/**
 * This represents many Group objects see above. Each group stores many users.
 *
 *
 * To Init this class pass in a list of Users you would like to organize into groups.
 *
 * @param users
 */
class Groups(users: List[User]) {

  private var _users = users.toBuffer;
  private var _groups : List[Group] = Nil

  //todo shuffer users.

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

      //todo: I have to make this better. This gets all left overs that won't fit the group size, and puts it into the last group.
      //it should randomly/evenly distribute the MOD throughout the process. This should make an even group sizes within the collection.
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

  /**
   * This is ALL the groups in this group object. It is a List so....do with it what you want. Parallelize, etc...
   */
  lazy val groups : List[Group] = {
    fillGroups()
  }

  //some debug stuff
  var groupCount : Int = groups.size
  System.out.println("groupCount")
  System.out.println(groupCount)

}