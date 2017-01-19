// build.sbt
//
scalaVersion := "2.12.1"

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "utf8",
  "-feature",
  "-unchecked",
  //"-Xfatal-warnings",
  //"-Xlint",
  //"-Ywarn-dead-code",
  //"-Ywarn-numeric-widen",
  //"-Ywarn-value-discard",
  //"-Xfuture",
  "-language", "postfixOps"
)

//--- Dependencies ---

val akkaVersion = "2.4.16"
val akkaActor = "com.typesafe.akka" %% "akka-actor" % akkaVersion

val akkaHttpVersion = "10.0.1"
val akkaHttp = "com.typesafe.akka" %% "akka-http" % akkaHttpVersion
val akkaHttpTestkit = "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test

val scalaTest = "org.scalatest" %% "scalatest" % "3.0.1" % Test

val dockerItScala = "com.whisk" %% "docker-testkit-scalatest" % "0.9.0-RC3" % Test    // 0.9.x needed for Scala 2.12

libraryDependencies ++= Seq(
  akkaHttp,
  //
  akkaHttpTestkit,
  scalaTest,
  dockerItScala
)
