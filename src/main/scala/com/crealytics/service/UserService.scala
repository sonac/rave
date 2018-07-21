package com.crealytics.service

import java.sql.{ResultSet, SQLException}
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import com.crealytics.DBConnector._
import com.crealytics.exasol._
import com.crealytics.common.ServiceHelpers._


case class UsersService(query: String) extends ExasolDelegator(conn) {
  val rs: Future[ResultSet] = exa.executeQuery(query)
  val rsl: Future[List[Map[String, String]]] = rs.map(queryToList(_))
  val fcols: Future[Iterable[String]] = rsl.map(_.head.keys)
  val users: Future[List[Option[UserService]]] = rsl.map { x =>
    val cols = x.head.keys
    x.map(m => listToCaseClass[UserService](rowMapToList(m, cols)))
  }

}

case class UserService(id: Int, name: String, birthDate: String)
