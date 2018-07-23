package com.crealytics.service

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import com.crealytics.common.ServiceHelpers._
import com.crealytics.models._


case class UserService(query: String) extends EntityService(query) {

  private val users: Future[List[Option[User]]] = rsl.map { x =>
    val cols = x.head.keys
    val t = x.map(m => listToCaseClass[User](rowMapToList(m, cols)))
    t
  }

  def getUsers: Future[List[Option[User]]] = users

  def getUser(uId: Int): Future[Option[User]] = {
    users.map{ ul =>
      ul.filter( optU => optU match {
        case Some(x) => x.id == uId
        case None => false
      }).head
    }
  }
}
