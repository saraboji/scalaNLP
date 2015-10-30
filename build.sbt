name := "scalaNLP";

version := "1.0.0";

scalaVersion := "2.11.7";

libraryDependencies ++= Seq(
    "org.clulab" %% "processors" % "5.7.0",
    "org.clulab" %% "processors" % "5.7.0" classifier "models",
	"org.easyrules" % "easyrules-core" % "2.1.0",
	"net.liftweb" %% "lift-json" % "2.6+"
)
libraryDependencies += "com.typesafe.akka" % "akka-actor_2.11" % "2.3.2"


resolvers ++= Seq(
  "ScalaNLP Maven2" at "http://repo.scalanlp.org/repo",
  "Scala Tools Snapshots" at "http://scala-tools.org/repo-snapshots/",
  "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
  "googlecode.hammurabi" at "http://hammurabi.googlecode.com/svn/repo/releases"
)