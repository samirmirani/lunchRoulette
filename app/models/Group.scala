package models

import scala.collection.mutable.ListBuffer
import util.Random._
import models.User


class Group(members: List[User]) {

  private var _members = members



}

class Groups(users: List[User]) {

  private var _users = users.toBuffer;
  private var _groups : List[Group] = Nil


  def groupSize : Int = {
    var random = scala.util.Random;
    val range = 3 to 4
    random.nextInt(range.length)
    3
  }

  /**
   * This will define the members of these groups.
   *
   * @param users
   * @return
   */
  def fillGroups() : Unit = {
    //This entire section can be done better. More functional, have to revist this.
    var userCount : Int = _users.size
    System.out.println("userCount")
    System.out.println(userCount)
    do {
      var sizeOfgroup = groupSize
      System.out.println("sizeOfgroup")
      System.out.println(sizeOfgroup)
      var members = if (_users.size <=  sizeOfgroup + 2) {
        //if there is a remainder in the group size just return the list. This way groups will maintain a minuim of at least 3
        val members = _users.toList
        _users = _users.drop(sizeOfgroup + 2)
        userCount = userCount - sizeOfgroup - 2
        members
      } else {
        //cut the list by the group size
        val members = _users.slice(0, sizeOfgroup).toList
        _users = _users.drop(sizeOfgroup)
        userCount = userCount - sizeOfgroup
        members
      }


      System.out.println("Members")
      System.out.println(members)
      _groups = _groups:+ new Group(members)
    } while (userCount > 0)
  }

  lazy val groups = {
    _groups
  }

  fillGroups()
  //some debug stuff
  var groupCount : Int = _groups.size
  System.out.println("groupCount")
  System.out.println(groupCount)

}