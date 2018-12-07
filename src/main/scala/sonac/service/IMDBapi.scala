package sonac.service

import com.typesafe.config.ConfigFactory
import scalaj.http._

object IMDBapi {
  private val config = ConfigFactory.load()
  private val apiKey = config.getString("IMDBapi.key")
  private val url = "http://imdbapi.net/api"

  def get(id: String, rType: String = "json"): String = {
    val params = Seq(
      ("key", apiKey),
      ("id", id),
      ("type", rType)
    )
    Http(url).postForm(params).asString.body
  }

}
