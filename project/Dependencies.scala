import sbt._

object Dependencies {

  // VERSIONS

  lazy val exasolJdbcV = "6.0.1"
  lazy val shapelessV = "2.3.3"
  lazy val akkaHttpV = "10.1.3"
  lazy val akkaCirceV = "1.21.0"
  lazy val sangriaV = "1.4.0"
  lazy val sangriaCirceV = "1.2.1"
  lazy val circe = "0.9.3"

  // DEPENDENCIES

  lazy val exasolJdbc = "com.exasol" % "exasol-jdbc" % exasolJdbcV
  lazy val shapeless =  "com.chuusai" %% "shapeless" % shapelessV
  lazy val akkaHttp = "com.typesafe.akka" %% "akka-http" % akkaHttpV
  lazy val akkaCirce = "de.heikoseeberger" %% "akka-http-circe" % akkaCirceV

  val coreDeps =
    Seq(
      exasolJdbc,
      shapeless,
      akkaHttp,
      akkaCirce
    )

}
