package com.crealytics.service


import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import com.crealytics.common.ServiceHelpers._
import com.crealytics.models._

case class MovieListService(query: String) extends EntityService(query) {

  private val movieLists: Future[List[Option[MovieList]]] = rsl.map { x =>
    val cols = x.head.keys
    val s = x.map(m => listToCaseClass[MovieList](rowMapToList(m, cols)))
    s
  }

  def getAllMovieLists: Future[List[Option[MovieList]]] = movieLists

  def getMovieListById(mlId: Int): Future[Option[MovieList]] = {
    movieLists.map{ mlL =>
      mlL.filter(optMl => optMl match {
        case Some(x) => x.mlId == mlId
        case None => false
      }).head
    }
  }

}
