package com.crealytics.models

import sangria.execution.deferred.{Fetcher, HasId}
import sangria.schema._

import scala.concurrent.Future
import scala.concurrent.duration._

import com.crealytics.service._

object SchemaDefinition {

  /*val entities: Fetcher[ExaRepo, Future[Option[User]], Future[Option[User]], Int] = Fetcher.caching(
    (ctx: ExaRepo, ids: Seq[Int]) =>
      Future.successful(ids.map(id => ctx.getUser(id))))(HasId(x => x.result(5 seconds).get.id))
*/
  val Entity: InterfaceType[ExaRepo, Entity] = {
    InterfaceType(
      "Entity",
      "An entity in the database",
      () => fields[ExaRepo, Entity](
        Field("id", IntType,
          Some("the id of the entity"),
          resolve = _.value.id)
      )
    )
  }

  val User = ObjectType(
    "User",
    "User in the db",
    interfaces[ExaRepo, User](Entity),
    fields[ExaRepo, User](
      Field("id", IntType,
        Some("user id"),
        resolve = _.value.id),
      Field("name", StringType,
        Some("user name"),
        resolve = _.value.name),
      Field("birthDate", StringType,
        Some("user birthdate"),
        resolve = _.value.birthDate)
    )
  )

  val MovieList = ObjectType(
    "MovieList",
    "Movie that user had put in his list",
    interfaces[ExaRepo, MovieList](Entity),
    fields[ExaRepo, MovieList](
      Field("mlId", IntType,
        Some("movie list id"),
        resolve = _.value.mlId),
      Field("movieName", StringType,
        Some("movie list name"),
        resolve = _.value.movieName),
      Field("movieRtng", FloatType,
        Some("movie rating from user"),
        resolve = _.value.movieRtng),
      Field("ownerId", IntType,
        Some("user id of movie list"),
        resolve = _.value.ownerId)
    )
  )

  val ID = Argument("id", IntType, description = "id of the user")

  val Query = ObjectType(
    "Query", fields[ExaRepo, Unit](
      Field("user", OptionType(User),
        arguments = ID :: Nil,
        resolve = ctx => ctx.ctx.getUser(ctx arg ID)
      ),
      Field("users", ListType(OptionType(User)),
        resolve = ctx => ctx.ctx.getAllUsers),
      Field("movieLists", ListType(OptionType(MovieList)),
        resolve = ctx => ctx.ctx.getAllMovieLists),
      Field("movieList", OptionType(MovieList),
        arguments = ID :: Nil,
        resolve = ctx => ctx.ctx.getMovieListById(ctx arg ID))
    )
  )

  val ExaSchema = Schema(Query)

}
