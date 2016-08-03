import sbt._

object PacktSubBuild extends Build {
  
  lazy val root = Project("root", file(".")) dependsOn(ssoup)
  lazy val ssoup = RootProject(uri("https://github.com/P3trur0/ssoup.git")) 
  
}