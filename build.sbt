// build.sbt
//
version := "0.0.1-SNAPSHOT"
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

val scalaTest = "org.scalatest" %% "scalatest" % "3.0.1" % Test

val dockerItScala = {
  val ver = "0.9.0"   // 0.9.x needed for Scala 2.12
  Seq(
    "com.whisk" %% "docker-testkit-scalatest" % ver % Test,
    "com.whisk" %% "docker-testkit-impl-docker-java" % ver % Test
  )
}

// This isn't publicly released (no release marked in GitHub, not found in Maven Central)
//
// Follow -> https://github.com/coreos/jetcd/issues/66
//
val jetcd = "com.coreos" % "jetcd" % "0.1.0-SNAPSHOT"

resolvers += Resolver.mavenLocal

libraryDependencies ++= Seq(
  jetcd,
  //
  scalaTest
) ++ dockerItScala
