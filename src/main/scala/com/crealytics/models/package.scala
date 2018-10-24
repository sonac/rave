package com.crealytics

import akka.http.scaladsl.model.DateTime
import sangria.execution.deferred.HasId

package object models {

  trait Identifiable {
    val id: Int
  }

  object Indentifiable {
    implicit def hasId[T <: Identifiable]: HasId[T, Int] = HasId(_.id)
  }

  case class Movie(id: Int, title: String, genres: String, imdb: String, posterLink: String, createdAt: DateTime = DateTime.now)
    extends Identifiable

  case class User(id: Int, username: String, password: String, eMail: String, createdAt: DateTime = DateTime.now)
    extends Identifiable

  case class UserMovies(id: Int, userId: Int, movieId: Int, place: String, comment: String, createdAt: DateTime = DateTime.now)
    extends Identifiable

}
