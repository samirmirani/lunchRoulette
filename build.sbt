name := """play-lunch"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala,  net.litola.SassPlugin)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws
)

libraryDependencies +=     "postgresql" % "postgresql" % "9.1-901.jdbc4"

libraryDependencies += "org.apache.commons" % "commons-email" % "1.3"

libraryDependencies += "com.typesafe.play.plugins" %% "play-plugins-mailer" % "2.3.1"