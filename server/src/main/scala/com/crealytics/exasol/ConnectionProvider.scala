
package com.crealytics.exasol

import com.crealytics.PropertiesImplicits._

import java.sql.{Connection, DriverManager, Statement}

/** Provides access to a database connection. */
sealed trait ConnectionProvider {
  def getConnection: Connection
  def releaseConnection(conn: Connection): Unit
}

/**
  * Handles EXAsol database connections.
  *
  * @constructor create a new ExasolConnectionProvider instance.
  * @param hosts where to reach EXAsol, e.g. `10.10.0.10..13`
  * @param port which port to use to connect to EXAsol
  * @param user the user to connect to EXAsol
  * @param password the password for the user to connect to EXAsol
  */
case class ExasolConnectionProvider(hosts: String, port: Int, user: String, password: String)
  extends ConnectionProvider {

  val connectionProperties: Map[String, String] =
    Map("user" -> user, "password" -> password, "driver" -> "com.exasol.jdbc.EXADriver")

  val url: String = s"jdbc:exa:$hosts:$port"

  /** Returns a new connection to EXAsol */
  def createConnection: Connection = DriverManager.getConnection(url, connectionProperties)

  override def getConnection: Connection = createConnection
  override def releaseConnection(conn: Connection) = conn.close
}