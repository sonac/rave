package com.crealytics

import com.crealytics.exasol.{ExasolConnectionProvider, ExasolDelegator}

object DBConnector {
  val conn = ExasolConnectionProvider("127.0.0.1", 8767, "sys", "exasol")
  val exa = new ExasolDelegator(conn)
}
