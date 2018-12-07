package sonac.models

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import slick.jdbc.PostgresProfile.api._
import DBSchema._
import sonac.common.PostgresConnector
import sonac.service.UserWithToken

trait DAO extends PostgresConnector {

  private val movieTable: TableQuery[MovieTable] = TableQuery[MovieTable]
  private val userTable: TableQuery[UserTable] = TableQuery[UserTable]
  private val userMoviesTable: TableQuery[UserMoviesTable] = TableQuery[UserMoviesTable]

  private val insertUserTable = userTable returning userTable.map(_.id) into ((u, id) => u.copy(id = id))
  private val insertMovieTable = movieTable returning movieTable.map(_.id) into ((u, id) => u.copy(id = id))

  def addMovie(title: String, genres: String, imdbLink: String): Future[Movie] = {
    db.run(insertMovieTable += Movie(0, title, genres, imdbLink))
  }

  def deleteMovie(id: Int): Future[Boolean] = {
    db.run(movieTable.filter(_.id === id).delete).map(_ == id)
  }

  def getMovieById(id: Int): Future[Option[Movie]] = {
    db.run(movieTable.filter(_.id === id).result.headOption)
  }

  def getAllMovies: Future[Seq[Movie]] = {
    db.run(movieTable.result)
  }

  def addUser(username: String, password: String, eMail: String): Future[UserWithToken] = {
    db.run(insertUserTable += User(0, username, password, eMail)).map(UserWithToken(_))
  }

  def deleteUser(id: Int): Future[Boolean] = {
    db.run(userTable.filter(_.id === id).delete).map(_ == id)
  }

  def getUserById(id: Int): Future[Option[User]] = {
    db.run(userTable.filter(_.id === id).result.headOption)
  }

  def getUserByUsername(username: String): Future[Option[User]] = {
    db.run(userTable.filter(_.username === username).result.headOption)
  }

  def getUser(username: String, password: String): Future[Option[User]] = {
    db.run(userTable.filter(u => u.username === username && u.password === password).result.headOption)
  }

  def getAllUsers: Future[Seq[User]] = {
    db.run(userTable.result)
  }

  def getUserMovies(uId: Int): Future[Seq[Movie]] = {
    db.run(userMoviesTable.filter(_.userId === uId)
      .join(movieTable).on(_.movieId === _.id)
      .result).map(_.map( _._2))
  }

}
