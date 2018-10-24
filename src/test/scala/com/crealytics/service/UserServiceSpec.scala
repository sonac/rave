package com.crealytics.service

import org.scalatest.FlatSpec

import scala.concurrent.Await
import scala.concurrent.duration._

class UserServiceSpec extends FlatSpec {

  val id = 2
  val userService = UserService(s"SELECT * FROM PG.USERS WHERE ID = $id")

  "UserService" should "return all users " in {
    val usrs = userService.getUsers

    val qs = Await.result(usrs, 5 seconds)
    println(qs)
  }

  "UserService" should "return precised user" in {
    val user = userService.getUser(2)

    val qs = Await.result(user, 5 seconds)
    println(qs)
  }

}
