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
    mainClass in compile := Some("com.crealytics.Server"),
    assemblyJarName in assembly := s"${name.value}.jar",
    test in assembly := {},
    unmanagedSourceDirectories in Compile += baseDirectory.value / "server" / "src" / "main" / "scala",
    unmanagedClasspath in Runtime += baseDirectory.value
  )
