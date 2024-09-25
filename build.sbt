name := "SmilesConverter"

version := "0.1"

scalaVersion := "2.13.13"

libraryDependencies ++= Seq(
  "dev.zio" %% "zio" % "2.1.4",
  "dev.zio" %% "zio-streams" % "2.1.6",
  "org.openscience.cdk" % "cdk-bundle" % "2.9",
  "uk.ac.cam.ch.opsin" % "opsin-core" % "2.7.0"
)
