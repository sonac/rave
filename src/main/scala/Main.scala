import java.sql._
import scala.concurrent.ExecutionContext.Implicits.global

import com.crealytics.DBConnector._
import com.crealytics.service.UsersService

object Main extends App {

  val rs = UsersService("SELECT t.* FROM PG.USERS t LIMIT 501")
  rs.users.map(x => x.foreach(println(_)))

}
