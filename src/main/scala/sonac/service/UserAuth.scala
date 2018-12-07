package sonac.service

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success, Try}

import akka.http.scaladsl.model.DateTime
import pdi.jwt.{Jwt, JwtAlgorithm}
import com.typesafe.config.ConfigFactory
import sonac.models.{DAO, User}

case class AuthenticationException(message: String) extends Exception(message)
case class AuthorisationException(message: String) extends Exception(message)

case class UserWithToken(username: String, eMail: String, createdAt: DateTime, token: String)

object UserWithToken extends UserAuth {
  def apply(user: User): UserWithToken = {
    UserWithToken(user.username, user.eMail, user.createdAt, baseEncode(user.username))
  }
}

trait UserAuth {

  private val config = ConfigFactory.load()
  private val secret = config.getString("secret")

  object server extends DAO

  private def baseDecode(valueToDecode: String): Try[String] = {
    Jwt.decodeRaw(valueToDecode, secret, Seq(JwtAlgorithm.HS256))
  }

  def baseEncode(valueToEncode: String): String = {
    val rr = Jwt.encode(valueToEncode, secret, JwtAlgorithm.HS256)
    println(rr)
    rr
  }

  def authenticate(username: String, password: String): Future[Option[UserWithToken]] = {
    server.getUser(username, baseEncode(password)).map{
      case Some(u) => Some(UserWithToken(u.username, u.eMail, u.createdAt, baseEncode(username)))
      case None => None
    }
  }

  def authorize(token: String): Future[Option[User]] = {
    baseDecode(token) match {
      case Success(value) => server.getUserByUsername(value)
      case Failure(exception) =>
        println(exception)
        Future(None)
    }
  }

}

