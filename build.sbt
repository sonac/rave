import Dependencies._

lazy val root = (project in file("."))
  .enablePlugins(GitVersioning)
  .settings(
    inThisBuild(List(
      organization := "com.crealytics",
      version      := "0.1.0",
      scalaVersion := "2.11.11"
    )),
    name := "sc-bi",
    resolvers ++= Seq(
      "ExasolMavenRepository" at "https://maven.exasol.com/artifactory/exasol-releases",
      Resolver.bintrayRepo("hseeberger", "maven")
  ),
    parallelExecution in Test := false,
    libraryDependencies ++= coreDeps,
    mainClass in assembly := Some("Main"),
    assemblyJarName in assembly := s"${name.value}.jar",
    test in assembly := {}
  )
