package com.crealytics.models

import akka.http.scaladsl.model.DateTime
import sangria.ast.StringValue
import sangria.execution.{ExceptionHandler, HandledException}
import sangria.execution.deferred._
import sangria.schema.{Field, ListType, ObjectType}
import sangria.schema._
import sangria.macros._
import sangria.macros.derive._
import sangria.marshalling.{CoercedScalaResultMarshaller, FromInput}
import sangria.validation.Violation

object SchemaDefinition {

  case object DateTimeCoerceViolation extends Violation {
    override def errorMessage: String = "Error parsing DateTime"
  }

  implicit val GraphQLDateTime: ScalarType[DateTime] = ScalarType[DateTime]("DateTime",
    coerceOutput = (dt, _) => dt.toString,
    coerceInput = {
      case StringValue(dt, _,_ , _, _) => DateTime.fromIsoDateTimeString(dt).toRight(DateTimeCoerceViolation)
      case _ => Left(DateTimeCoerceViolation)
    },
    coerceUserInput = {
      case s: String => DateTime.fromIsoDateTimeString(s).toRight(DateTimeCoerceViolation)
      case _ => Left(DateTimeCoerceViolation)
    }
  )

  val Movie: ObjectType[Unit, Movie] = deriveObjectType[Unit, Movie]()
  val User: ObjectType[Unit, User] = deriveObjectType[Unit, User]()

  val byUser: Relation[Movie, Movie, Movie] = Relation[Movie, Movie]("byUser", c => Seq(c))

  val ID = Argument("id", IntType, description = "id of the user")
  val Title = Argument("title", StringType, description = "movie title")
  val Genre = Argument("genre", StringType, description = "movie genre")
  val IMDBLink = Argument("IMDBLink", StringType, description = "link to imdb page")

  val Query = ObjectType(
    "Query", fields[DAO, Unit](
      Field("movie", OptionType(Movie),
        arguments = ID :: Nil,
        resolve = ctx => ctx.ctx.getMovieById(ctx arg ID)),
      Field("movies", ListType(Movie),
        resolve = ctx => ctx.ctx.getAllMovies),
      Field("user", OptionType(User),
        arguments = ID :: Nil,
        resolve = ctx => ctx.ctx.getUserById(ctx arg ID)),
      Field("users", ListType(User),
        resolve = ctx => ctx.ctx.getAllUsers),
      Field("userMovies", ListType(Movie),
        arguments = ID :: Nil,
        resolve = ctx => ctx.ctx.getUserMovies(ctx arg ID))
    )
  )

  val Mutation = ObjectType(
    "Mutation", fields[DAO, Unit](
      Field("addMovie", IntType,
        arguments = Title :: Genre :: IMDBLink :: Nil,
        resolve = ctx => ctx.ctx.addMovie(ctx.arg(Title) , ctx.arg(Genre), ctx.arg(IMDBLink)))
    )
  )

  val DBSchema = Schema(Query, Some(Mutation))

}
