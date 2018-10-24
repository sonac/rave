package com.crealytics.common

import com.typesafe.config.ConfigFactory
import slick.driver.PostgresDriver.api._

class PostgresConnector {
  val conf = ConfigFactory.load()
  val db = Database.forConfig("pg", config = conf)
  println(db.createSession().database)
}
