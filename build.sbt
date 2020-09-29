import sbtcrossproject.CrossType

organization := "com.joshkapple"
lazy val projectVersion = "1.0-SNAPSHOT"
lazy val scalaV = "2.13.0"

scalaVersion := scalaV

lazy val root = Project(id = "tic-tac-toe",
  base = file(".")).
  aggregate(shared.js,shared.jvm).
  settings(
    publish := {},
    publishLocal := {},
  )

lazy val shared = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("shared"))
  .settings(name := "shared",
    version := projectVersion,
    scalaVersion := scalaV,
    scalacOptions ++= Seq("-Ymacro-annotations"),
    libraryDependencies ++= Seq(
      "com.lihaoyi" %% "upickle" % "0.9.9",
      "com.beachape" %%% "enumeratum" % "1.6.1"
    )
  )
  .jsConfigure(_ enablePlugins ScalaJSWeb)

lazy val server = (project in file("server"))
  .settings(
  name := "server",
  version := projectVersion,
  scalaVersion := scalaV,
    scalaJSProjects := Seq(client),
    pipelineStages in Assets := Seq(scalaJSPipeline),
  libraryDependencies ++= Seq(
    guice,
    "com.vmunier" %% "scalajs-scripts" % "1.1.4",
    "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test,
    "com.typesafe.play" %% "play-json" % "2.7.4",
    "com.lihaoyi" %% "autowire" % "0.3.2"
  )
  ).enablePlugins(PlayScala)
  .dependsOn(shared.jvm)

lazy val client = (project in file("client")).
  settings(
    name := "client",
    version := projectVersion,
    scalacOptions ++= Seq("-Xxml:coalescing", "-Ymacro-annotations"),
    scalaJSUseMainModuleInitializer := true,
    mainClass in Compile := Some("client.TicTacToeApp"),
    scalaVersion := scalaV,
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "0.9.8",
      "com.typesafe.play" %% "play-json" % "2.7.4",
      "com.lihaoyi" %%% "autowire" % "0.3.2",
      "com.lihaoyi" %%% "upickle" % "0.9.9",
      "org.lrng.binding" %%% "html" % "1.0.3",
      "com.thoughtworks.binding" %%% "futurebinding" % "12.0.0"))
  .dependsOn(shared.js)
  .enablePlugins(ScalaJSPlugin, ScalaJSWeb)
