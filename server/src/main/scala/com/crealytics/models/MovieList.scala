package com.crealytics.models

case class MovieList(mlId: Int, movieName: String, movieRtng: Double, ownerId: Int) extends Entity {
  def id: Int = mlId
}