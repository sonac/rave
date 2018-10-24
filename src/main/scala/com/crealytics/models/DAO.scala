package com.crealytics.models

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import slick.jdbc.PostgresProfile.api._

import com.crealytics.common.PostgresConnector
import DBSchema._

class DAO extends PostgresConnector {

  private val movieTable: TableQuery[MovieTable] = TableQuery[MovieTable]
  private val userTable: TableQuery[UserTable] = TableQuery[UserTable]
  private val userMoviesTable: TableQuery[UserMoviesTable] = TableQuery[UserMoviesTable]

  def addMovie(title: String, genres: String, imdbLink: String): Future[Int] = {
    val res = db.run(movieTable.map{ mt =>
      (mt.title, mt.genres, mt.imdb)
    } += (title, genres, imdbLink))
    res.foreach(println(_))
    res
  }

  def deleteMovie(id: Int): Future[Boolean] = {
    db.run(movieTable.filter(_.id === id).delete).map(_ == id)
  }

  def getMovieById(id: Int): Future[Option[Movie]] = {
    db.run(movieTable.filter(_.id === id).result.headOption)
  }

  def getAllMovies: Future[Seq[Movie]] = {
    db.run(movieTable.result)
  }

  def addUser(user: User): Future[Int] = {
    db.run(userTable += user)
  }

  def deleteUser(id: Int): Future[Boolean] = {
    db.run(userTable.filter(_.id === id).delete).map(_ == id)
  }

  def getUserById(id: Int): Future[Option[User]] = {
    db.run(userTable.filter(_.id === id).result.headOption)
  }

  def getAllUsers: Future[Seq[User]] = {
    db.run(userTable.result)
  }

  def getUserMovies(uId: Int): Future[Seq[Movie]] = {
    db.run(userMoviesTable.filter(_.userId === uId)
      .join(movieTable).on(_.movieId === _.id)
      .result).map(_.map( _._2))
  }

}
