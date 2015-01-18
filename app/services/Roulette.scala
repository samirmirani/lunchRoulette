package services

import models.User

import scala.util.Random

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
    //randomize this list.
    val shuffledUsers : List[User] = Random.shuffle(users)


    val halfOfListCount = shuffledUsers.size / 2;

    val groupOne = shuffledUsers.slice(0, halfOfListCount)
    val groupTwo = shuffledUsers.slice(halfOfListCount, shuffledUsers.size)


    //this will pair everyone together.
    val pairUsers = groupOne zip groupTwo
    pairUsers
  }
}