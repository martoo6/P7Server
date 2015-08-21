import _root_.sbtrelease.ReleasePlugin._
import sbtrelease.ReleasePlugin._

releaseSettings

name := """scala-thrust"""

version := "1.0"

scalaVersion := "2.11.7"

crossScalaVersions := Seq("2.10.4", scalaVersion.value)

lazy val root = (project in file(".")).enablePlugins(SbtWeb)

libraryDependencies += "io.argonaut" %% "argonaut" % "6.1"

libraryDependencies += "com.typesafe" % "config" % "1.2.1"

libraryDependencies += "org.mashupbots.socko" %% "socko-webserver" % "0.6.0"

libraryDependencies += "org.mashupbots.socko" %% "socko-rest" % "0.6.0"

libraryDependencies += "org.scala-lang" % "scala-compiler" % "2.11.7"

libraryDependencies += "org.scala-sbt" % "command" % "0.13.9" exclude("jline", "jline")

libraryDependencies += "commons-io" % "commons-io" % "2.4"

