name := """scala-thrust"""

version := "1.0"

scalaVersion := "2.11.5"

crossScalaVersions := Seq("2.10.4", scalaVersion.value)

libraryDependencies += "io.argonaut" %% "argonaut" % "6.0.4"

libraryDependencies += "com.typesafe" % "config" % "1.2.1"

libraryDependencies += "org.mashupbots.socko" %% "socko-webserver" % "0.6.0"

libraryDependencies += "org.scala-lang" % "scala-compiler" % "2.11.5"

libraryDependencies += "org.scala-sbt" % "command" % "0.13.9"