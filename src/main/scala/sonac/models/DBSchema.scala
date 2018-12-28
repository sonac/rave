package sonac.models

import java.sql.Timestamp

import akka.http.scaladsl.model.DateTime
import slick.ast.BaseTypedType
import slick.jdbc.JdbcType
import slick.jdbc.PostgresProfile.api._

object DBSchema {

  implicit val dateTimeColumnType: JdbcType[DateTime] with BaseTypedType[DateTime] = MappedColumnType.base[DateTime, Timestamp](
    dt => new Timestamp(dt.clicks),
    ts => DateTime(ts.getTime)
  )

  class MovieTable(tag: Tag) extends Table[Movie](tag, Some("rave"),"movies") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def title = column[String]("title")
    def genres = column[String]("genres")
    def imdb = column[String]("imdb")
    def posterLink = column[String]("poster_link")
    def createdAt = column[DateTime]("created_at")
    def * = (id, title, genres, imdb, posterLink, createdAt).mapTo[Movie]
  }

  class UserTable(tag: Tag) extends Table[User](tag, Some("rave"), "users") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def username = column[String]("username", O.Unique)
    def password = column[String]("password")
    def email = column[String]("email", O.Unique)
    def createdAt = column[DateTime]("created_at")
    def * = (id, username, password, email, createdAt).mapTo[User]
  }

  class UserMoviesTable(tag: Tag) extends Table[UserMovies](tag, Some("rave"), "user_movies") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def userId = column[Int]("user_id")
    def movieId = column[Int]("movie_id")
    def place = column[String]("place")
    def comment = column[String]("comment")
    def createdAt = column[DateTime]("created_at")
    def * = (id, userId, movieId, place, comment, createdAt).mapTo[UserMovies]
  }

}

