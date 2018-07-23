package com.crealytics.service

import java.sql.ResultSet

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import com.crealytics.DBConnector.exa
import com.crealytics.exasol.ExasolDelegator
import com.crealytics.DBConnector._


class EntityService(query: String) extends ExasolDelegator(conn) {

  protected val rs: Future[ResultSet] = exa.executeQuery(query)
  protected val rsl: Future[List[Map[String, String]]] = rs.map(queryToList(_))
  protected val fcols: Future[Iterable[String]] = rsl.map(_.head.keys)

}