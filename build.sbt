import sbt.Resolver
name := "Scala Hello World"

scalaVersion := "2.12.6" // or any other Scala version >= 2.10.2

resolvers += Resolver.bintrayRepo("wrotki", "maven-repo")

enablePlugins(ScalaJSPlugin)

enablePlugins(ScalaJSBundlerPlugin)

scalaJSUseMainModuleInitializer := true

scalacOptions ++= Seq("-Ypartial-unification"
//  , "-Xprint:typer"
)

npmDependencies in Compile += "aws-sdk" -> "2.315.0"

jsDependencies += ProvidedJS / "glob.js"

val circeVersion = "0.9.3"

libraryDependencies ++= Seq(
  "io.circe" %%% "circe-core",
  "io.circe" %%% "circe-generic",
  "io.circe" %%% "circe-parser"
).map(_ % circeVersion)

libraryDependencies ++= Seq(
  "io.wrotki" %%% "scala-js-awssdk" % "1.0.2", 
  "com.lihaoyi" %%% "utest" % "0.4.8" % "test",
   "org.typelevel" %%% "cats-core" % "1.4.0",
  "org.typelevel" %%% "cats-effect" % "1.0.0"
)

testFrameworks += new TestFramework("utest.runner.Framework")
