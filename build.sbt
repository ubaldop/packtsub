name := "PacktSub"

version := "0.0.1"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
			"com.typesafe" % "config" % "1.3.0",
			"com.eed3si9n" %% "gigahorse-core" % "0.1.1",
			"org.apache.commons" % "commons-email" % "1.4",
			"org.scalatest" %% "scalatest" % "3.0.0" % "test",
			"org.slf4j" % "slf4j-nop" % "1.7.21"
			)
			
def stagingClean = Command.command("staging-clean"){currentState =>
	val homeDir = sys.env("HOME")
	val k = ("rm -rf "+ homeDir + "/.sbt/0.13/staging/").!
	currentState
}		

commands ++= Seq(stagingClean)

enablePlugins(JavaAppPackaging)
mainClass in Compile := Some("com.p3trur0.packtsub.PacktSub")