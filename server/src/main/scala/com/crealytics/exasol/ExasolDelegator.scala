package com.crealytics.exasol

import java.sql.{Connection, ResultSet, SQLException, Statement}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import com.crealytics.common.Retry


/**
  * Deals with firing queries to EXAsol.
  *
  * @constructor create a new ExasolDelegator instance.
  * @param connectionProvider to retrieve an EXAsol database connection
  */
class ExasolDelegator(connectionProvider: ConnectionProvider) {

  /**
    * Creates a connection and passes it on to the run method.
    *
    * The connection will be automatically closed afterwards.
    *
    * @tparam T type of result
    * @param run the actual method to run
    * @return the result of the run call
    */
  def withConnection[T](run: Connection => T): T = {
    val conn = connectionProvider.getConnection
    try {
      run(conn)
    } finally {
      connectionProvider.releaseConnection(conn)
    }
  }

  /**
    * Changes the connection to no-autocommit mode, passes the transaction-enabled
    * connection to the run method.
    *
    * The autocommit mode will be turned on automatically again. If there is no
    * connection passed, one will be created for this single call.
    *
    * @tparam T type of result
    * @param run the actual method to run
    * @return the result of the run call
    */
  def withTransaction[T](run: Connection => T): T = withConnection { conn =>
    conn.setAutoCommit(false)
    try {
      run(conn)
    } finally {
      conn.setAutoCommit(true)
    }
  }

  /**
    * Creates a new statement and passes it on to the run method.
    *
    * The statement will be closed automatically. A connection is
    * created for this single call.
    *
    * On success the provided paths list is watermarked.
    *
    * TODO - make it more generic with paths
    *
    * @tparam T type of result
    * @param run the actual method to run
    * @param paths list of path URIs involved in the given query that should be watermarked on query success
    * @return the result of the run call
    */
  def withStatement[T](run: Statement => T)(paths: Seq[String]): T = withConnection { conn =>
    val stmt = conn.createStatement
    try {
      val result = run(stmt)
      if (paths.nonEmpty) paths
      result
    } finally {
      stmt.close()
    }
  }

  /**
    * Creates a new statement and passes it on to the run method.
    * Runs the query using the provided connection (e.g. altered for transactional behavior).
    *
    * The statement will be closed automatically.
    *
    * On success the provided paths list is watermarked.
    *
    * TODO - make it more generic with paths
    *
    * @tparam T type of result
    * @param conn connection to be provided, e.g. in case the connection needs to be altered for transactional
    *             behavior or other session parameters (e.g. query runtime limits, timezone etc.)
    * @param run the actual method to run
    * @param paths list of path URIs involved in the given query that should be watermarked on query success
    * @return the result of the run call
    */
  def withStatementProvidedConnection[T](conn: Connection)(run: Statement => T)(paths: Seq[String]): T = {
    val stmt = conn.createStatement
    try {
      val result = run(stmt)
      if (paths.nonEmpty) paths
      result
    } finally {
      stmt.close()
    }
  }

  def executeUpdate(query: String, paths: Seq[String], retries: Int = 4): Long = Retry.retry[Long, SQLException](retries) {
    withStatement[Long](stmt => stmt.executeUpdate(query))(paths)
  }

  def executeUpdates(queries: Seq[(String, Seq[String])], retries: Int = 4): Seq[Long] = Retry.retry[Seq[Long], SQLException](retries) {
    queries.foldLeft(Seq.empty[Long]) { (acc: Seq[Long], query: (String, Seq[String])) =>
      acc :+ withStatement[Long](stmt => stmt.executeUpdate(query._1))(query._2)
    }
  }

  def executeUpdatesInTransaction(queries: Seq[(String, Seq[String])], retries: Int = 4): Seq[Long] = Retry.retry[Seq[Long], SQLException](retries) {
    withTransaction[Seq[Long]] { conn =>
      queries.foldLeft(Seq.empty[Long]) { (acc: Seq[Long], query: (String, Seq[String])) =>
        acc :+ withStatementProvidedConnection[Long](conn)(stmt => stmt.executeUpdate(query._1))(query._2)
      }
    }
  }

  def executeQuery(query: String, retries: Int = 4): Future[ResultSet] = Future(Retry.retry[ResultSet, SQLException](retries) {
    withStatement[ResultSet](stmt => stmt.executeQuery(query))(Seq())
  })

  def execute(query: String, retries: Int = 4): Boolean = Retry.retry[Boolean, SQLException](retries) {
    withStatement[Boolean](stmt => stmt.execute(query))(Seq())
  }

  def queryToList(rs: ResultSet): List[Map[String, String]] = {
    val metadata = rs.getMetaData
    val columns = metadata.getColumnCount
    Iterator
      .continually(rs.next)
      .takeWhile(identity)
      .map { _ =>
        (for (i <- 1 to columns) yield {
          val key = metadata.getColumnName(i).toUpperCase
          val value = rs.getObject(i)
          rs.wasNull() match {
            case true => (key -> null)
            case _ => (key -> value.toString)
          }
        }).toMap
      }
      .toList
  }

}
