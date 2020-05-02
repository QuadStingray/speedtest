name := "speed4s"

organization := "com.quadstingray"

version := "0.1.0"

scalaVersion := "2.13.2"

homepage := Some(url("https://quadstingray.github.io/speed4s/"))

scmInfo := Some(ScmInfo(url("https://github.com/QuadStingray/speed4s"), "https://github.com/QuadStingray/speed4s.git"))

developers := List(Developer("QuadStingray", "QuadStingray", "github@quadstingray.com", url("https://github.com/QuadStingray")))

licenses += ("Apache-2.0", url("https://github.com/QuadStingray/speed4s/blob/master/LICENSE"))


libraryDependencies += "com.squareup.okhttp3" % "okhttp" % "4.6.0"

libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"

libraryDependencies += "org.json4s" %% "json4s-native" % "3.7.0-M2"