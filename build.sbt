name := "speedtest"

organization := "com.quadstingray"

scalaVersion := crossScalaVersions.value.last

crossScalaVersions := List("2.13.1", "2.13.2")

bintrayReleaseOnPublish in ThisBuild := true

libraryDependencies += "com.squareup.okhttp3" % "okhttp" % "4.6.0"

libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"

libraryDependencies += "com.typesafe" % "config" % "1.4.0"

libraryDependencies ++= Seq(
  "com.github.plokhotnyuk.jsoniter-scala" %% "jsoniter-scala-core"   % "2.2.1",
  "com.github.plokhotnyuk.jsoniter-scala" %% "jsoniter-scala-macros" % "2.2.1"
)
