package sonac.service

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success, Try}
import akka.http.scaladsl.model.DateTime
import pdi.jwt.{Jwt, JwtAlgorithm}
import com.typesafe.config.ConfigFactory
import sangria.execution.{ExceptionHandler, HandledException}
import sonac.models.{DAO, User}

case class AuthenticationException(message: String) extends Exception(message)
case class AuthorisationException(message: String) extends Exception(message)
case class RegisterException(message: String) extends Exception(message)

case class UserWithToken(username: String, eMail: String, createdAt: DateTime, token: String)

object UserWithToken extends UserAuth {
  def apply(user: User): UserWithToken = {
    UserWithToken(user.username, user.eMail, user.createdAt, baseEncode(user.username))
  }
}

trait UserAuth {

  private val config = ConfigFactory.load()
  private val secret = config.getString("secret")

  object dbServer extends DAO

  val errorHandler = ExceptionHandler {
    case (m, AuthenticationException(message)) ⇒ HandledException(message)
    case (m, AuthorisationException(message)) ⇒ HandledException(message)
    case (m, RegisterException(message)) => HandledException(message)
  }

  private def baseDecode(valueToDecode: String): Try[String] = {
    Jwt.decodeRaw(valueToDecode, secret, Seq(JwtAlgorithm.HS256))
  }

  def baseEncode(valueToEncode: String): String = {
    Jwt.encode(valueToEncode, secret, JwtAlgorithm.HS256)
  }

  def authenticate(email: String, password: String): Future[Option[UserWithToken]] = {
    dbServer.getUser(email, baseEncode(password)).map{
      case Some(u) => Some(UserWithToken(u.username, u.eMail, u.createdAt, baseEncode(u.username)))
      case None => throw new AuthenticationException("Username or password is incorrect")
    }
  }

  def authorize(token: String): Future[Option[User]] = {
    baseDecode(token) match {
      case Success(value) => dbServer.getUserByUsername(value)
      case Failure(exception) =>
        println(exception)
        Future(None)
    }
  }

  def register(username: String, email: String, password: String): Future[UserWithToken] = {
    dbServer.addUser(username, email, password).map(_ match {
      case Success(u) => UserWithToken(u)
      case Failure(_) => throw new RegisterException("User with such email or username already exists")
    })
  }

}

