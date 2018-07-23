import sbt._

object Dependencies {

  // VERSIONS

  lazy val exasolJdbcV = "6.0.1"
  lazy val shapelessV = "2.3.3"
  lazy val akkaHttpV = "10.1.3"
  lazy val akkaCirceV = "1.21.0"
  lazy val sangriaV = "1.4.0"
  lazy val sangriaCirceV = "1.2.1"
  lazy val circeV = "0.9.3"
  lazy val scalaTestV = "3.0.5"

  // DEPENDENCIES

  lazy val exasolJdbc = "com.exasol" % "exasol-jdbc" % exasolJdbcV
  lazy val shapeless =  "com.chuusai" %% "shapeless" % shapelessV
  lazy val akkaHttp = "com.typesafe.akka" %% "akka-http" % akkaHttpV
  lazy val akkaCirce = "de.heikoseeberger" %% "akka-http-circe" % akkaCirceV
  lazy val sangria = "org.sangria-graphql" %% "sangria" % sangriaV
  lazy val sangriaCirce = "org.sangria-graphql" %% "sangria-circe" % sangriaCirceV
  lazy val circeCore = "io.circe" %%	"circe-core" % circeV
  lazy val circeParser = "io.circe" %%	"circe-parser" % circeV
  lazy val circeOptics = "io.circe" %%	"circe-optics" % circeV

  lazy val scalaTest = "org.scalatest" %% "scalatest" % scalaTestV % Test

  val coreDeps =
    Seq(
      exasolJdbc,
      shapeless,
      akkaHttp,
      akkaCirce,
      sangria,
      sangriaCirce,
      circeCore,
      circeParser,
      circeOptics,
      scalaTest
    )

}
