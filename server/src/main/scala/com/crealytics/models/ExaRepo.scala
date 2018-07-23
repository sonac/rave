package com.crealytics.models

import com.crealytics.service._

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Success, Failure}

class ExaRepo {

  def getAllUsers: Future[List[Option[User]]] = {
    val us = UserService("SELECT * FROM PG.USERS")
    us.getUsers
  }

  def getUser(id: Int): Future[Option[User]] = {
    val us = UserService(s"SELECT * FROM PG.USERS WHERE ID = $id")
    us.getUser(id)
  }

  def getAllMovieLists: Future[List[Option[MovieList]]] = {
    val mls = MovieListService("SELECT * FROM PG.MOVIE_LISTS")
    mls.getAllMovieLists
  }

  def getMovieListById(mlId: Int): Future[Option[MovieList]] = {
    val mls = MovieListService(s"SELECT * FROM PG.MOVIE_LISTS WHERE ML_ID = $mlId")
    mls.getMovieListById(mlId)
  }
}

trait Entity {
  def id: Int
}
