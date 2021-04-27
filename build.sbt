name := "speedtest"

organization := "com.quadstingray"

scalaVersion := crossScalaVersions.value.last

crossScalaVersions := List("2.13.5")

bintrayReleaseOnPublish in ThisBuild := true

libraryDependencies += "com.squareup.okhttp3" % "okhttp" % "4.9.1"

libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.3"

libraryDependencies += "com.typesafe" % "config" % "1.4.1"

val Jsoniter_Version = "2.7.2"

libraryDependencies ++= Seq(
  "com.github.plokhotnyuk.jsoniter-scala" %% "jsoniter-scala-core"   % Jsoniter_Version,
  "com.github.plokhotnyuk.jsoniter-scala" %% "jsoniter-scala-macros" % Jsoniter_Version
)
