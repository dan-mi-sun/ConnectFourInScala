name := "Connect Four"

organization := "com.danielsan"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.11.2"

crossScalaVersions := Seq("2.10.4", "2.11.2")

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.1",
  "org.scalacheck" %% "scalacheck" % "1.11.5"
)

initialCommands := "import com.danielsan.connectfour._"
