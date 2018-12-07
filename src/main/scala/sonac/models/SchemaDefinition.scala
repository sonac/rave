package sonac.models

import akka.http.scaladsl.model.DateTime
import sangria.ast.StringValue
import sangria.execution.{ExceptionHandler, HandledException}
import sangria.execution.deferred._
import sangria.schema.{Field, ListType, ObjectType}
import sangria.schema._
import sangria.macros.derive._
import sangria.validation.Violation
import sonac.service.{AuthenticationException, AuthorisationException, UserAuth, UserWithToken}

object SchemaDefinition {

  class QueryContext extends DAO with UserAuth

  case object DateTimeCoerceViolation extends Violation {
    override def errorMessage: String = "Error parsing DateTime"
  }

  val errorHandler = ExceptionHandler {
    case (m, AuthenticationException(message)) ⇒ HandledException(message)
    case (m, AuthorisationException(message)) ⇒ HandledException(message)
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
  val UserWithToken: ObjectType[Unit, UserWithToken] = deriveObjectType[Unit, UserWithToken]()

  val byUser: Relation[Movie, Movie, Movie] = Relation[Movie, Movie]("byUser", c => Seq(c))

  val ID = Argument("id", IntType, description = "id of the user")
  val Title = Argument("title", StringType, description = "movie title")
  val Genre = Argument("genre", StringType, description = "movie genre")
  val IMDBLink = Argument("IMDBLink", StringType, description = "link to imdb page")
  val Username = Argument("username", StringType, description = "user name")
  val Password = Argument("password", StringType, description = "password")
  val EMail = Argument("email", StringType, description = "email")
  val Token = Argument("token", StringType, description = "token")

  val Query = ObjectType(
    "Query", fields[QueryContext, Unit](
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
        resolve = ctx => ctx.ctx.getUserMovies(ctx arg ID)),
      Field("authenticate", OptionType(UserWithToken),
        arguments = Username :: Password :: Nil,
        resolve = ctx => ctx.ctx.authenticate(ctx arg Username, ctx arg Password)),
      Field("authorize", OptionType(User),
        arguments = Token :: Nil,
        resolve = ctx => ctx.ctx.authorize(ctx arg Token))
    )
  )

  val Mutation = ObjectType(
    "Mutation", fields[QueryContext, Unit](
      Field("addMovie", Movie,
        arguments = Title :: Genre :: IMDBLink :: Nil,
        resolve = ctx => ctx.ctx.addMovie(ctx.arg(Title) , ctx.arg(Genre), ctx.arg(IMDBLink))),
      Field("addUser", UserWithToken,
        arguments = Username :: Password :: EMail :: Nil,
        resolve = ctx => ctx.ctx.addUser(ctx.arg(Username) , ctx.ctx.baseEncode(ctx.arg(Password)), ctx.arg(EMail)))
    )
  )

  val DBSchema = Schema(Query, Some(Mutation))

}
