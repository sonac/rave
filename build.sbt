import Dependencies._



lazy val root = (project in file("."))
  .enablePlugins(GitVersioning)
  .settings(
    inThisBuild(List(
      organization := "sonac",
      version      := "0.1.0",
      scalaVersion := "2.12.7"
    )),
    name := "rave",
    resolvers ++= Seq(
      Resolver.bintrayRepo("hseeberger", "maven")
  ),
    parallelExecution in Test := false,
    libraryDependencies ++= coreDeps,
    mainClass in (Compile, run) := Some("sonac.Server"),
    assemblyJarName in assembly := s"${name.value}.jar",
    test in assembly := {},
    fullClasspath in Runtime += baseDirectory.value / "public"
  )