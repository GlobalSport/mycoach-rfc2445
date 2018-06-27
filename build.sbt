name := "mycoach-rfc2445"

organization := "com.mycoachsport"

version := IO.read(new File("VERSION")).mkString.trim + "-SNAPSHOT"

scalaVersion := "2.12.6"

crossScalaVersions := Seq("2.11.12", "2.12.6")

isSnapshot := true

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.4" % Test
)