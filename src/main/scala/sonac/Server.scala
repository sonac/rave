package sonac

import scala.util.control.NonFatal
import scala.util.{Failure, Success}

import sangria.ast.Document
import sangria.execution.{ErrorWithResolver, Executor, QueryAnalysisError}
import sangria.parser.{QueryParser, SyntaxError}
import sangria.parser.DeliveryScheme.Try
import sangria.marshalling.circe._
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.MediaTypes._
import akka.http.scaladsl.model.headers.Cookie
import akka.http.scaladsl.server._
import akka.stream.ActorMaterializer
import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport._
import io.circe._
import io.circe.optics.JsonPath._
import io.circe.parser._
import common.GraphQLRequestUnmarshaller._

import sonac.models.SchemaDefinition._
import sonac.models.SchemaDefinition
import sonac.service.IMDBapi

import scala.io.StdIn

object Server extends App {

  implicit val system: ActorSystem = ActorSystem("exa-server")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  //implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  import system.dispatcher

  def executeGraphQL(query: Document, operationName: Option[String], variables: Json) = {
    complete(Executor.execute(SchemaDefinition.DBSchema, query, new QueryContext,
      variables = if (variables.isNull) Json.obj() else variables,
      operationName = operationName)
      .map(OK -> _)
      .recover {
        case error: QueryAnalysisError => BadRequest -> error.resolveError
        case error: ErrorWithResolver => InternalServerError -> error.resolveError
      })
  }

  def formatError(error: Throwable): Json = error match {
    case syntaxError: SyntaxError =>
      Json.obj("errors" -> Json.arr(
        Json.obj(
          "message" -> Json.fromString(syntaxError.getMessage),
          "locations" -> Json.arr(Json.obj(
            "line" -> Json.fromBigInt(syntaxError.originalError.position.line),
            "column" -> Json.fromBigInt(syntaxError.originalError.position.column)
          ))
        )
      ))
    case NonFatal(e) => formatError(e)
    case e => throw e
  }

  def formatError(message: String): Json =
    Json.obj("errors" -> Json.arr(Json.obj("message" -> Json.fromString(message))))

  val route: Route =
    //graphql endpoint
    path("graphql") {
      get {
        explicitlyAccepts(`text/html`) {
          getFromResource("assets/graphiql.html")
        } ~
          parameters('query, 'operationName.?, 'variables.?) { (query, operationName, variables) ⇒
            QueryParser.parse(query) match {
              case Success(ast) ⇒
                variables.map(parse) match {
                  case Some(Left(error)) ⇒ complete(BadRequest, formatError(error))
                  case Some(Right(json)) ⇒ executeGraphQL(ast, operationName, json)
                  case None ⇒ executeGraphQL(ast, operationName, Json.obj())
                }
              case Failure(error) ⇒ complete(BadRequest, formatError(error))
            }
          }
      } ~
        post {
          parameters('query.?, 'operationName.?, 'variables.?) { (queryParam, operationNameParam, variablesParam) ⇒
            entity(as[Json]) { body ⇒
              val query = queryParam orElse root.query.string.getOption(body)
              val operationName = operationNameParam orElse root.operationName.string.getOption(body)
              val variablesStr = variablesParam orElse root.variables.string.getOption(body)

              query.map(QueryParser.parse(_)) match {
                case Some(Success(ast)) ⇒
                  variablesStr.map(parse) match {
                    case Some(Left(error)) ⇒ complete(BadRequest, formatError(error))
                    case Some(Right(json)) ⇒ executeGraphQL(ast, operationName, json)
                    case None ⇒ executeGraphQL(ast, operationName, root.variables.json.getOption(body) getOrElse Json.obj())
                  }
                case Some(Failure(error)) ⇒ complete(BadRequest, formatError(error))
                case None ⇒ complete(BadRequest, formatError("No query to execute"))
              }
            } ~
              entity(as[Document]) { document ⇒
                variablesParam.map(parse) match {
                  case Some(Left(error)) ⇒ complete(BadRequest, formatError(error))
                  case Some(Right(json)) ⇒ executeGraphQL(document, operationNameParam, json)
                  case None ⇒ executeGraphQL(document, operationNameParam, Json.obj())
                }
              }
          }
        }
    } ~
    //routes to front-end logic
    pathSingleSlash {
      getFromFile("public/index.html")
    } ~
    path("add-movie") {
      getFromFile("public/index.html")
    } ~
    path("registration") {
      getFromFile("public/index.html")
    } ~
    path("login") {
      getFromFile("public/index.html")
    } ~
    //static files routes
    path("bundle.js") {
      get {
        getFromFile("public/bundle.js")
      }
    } ~
    path("images" / Segment ) { img =>
      getFromFile(s"public/images/$img")
    }

  //print(IMDBapi.get("tt4779682"))

  val server = Http().bindAndHandle(route, "0.0.0.0", sys.props.get("http.port").fold(8080)(_.toInt))

  StdIn.readLine()

  server.flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => system.terminate())

}
