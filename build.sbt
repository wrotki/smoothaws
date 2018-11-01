import Projects.Versions
import sbt.{Resolver, ScmInfo}

name := "smoothaws"

scalaVersion := "2.12.6" // or any other Scala version >= 2.10.2

resolvers += Resolver.jcenterRepo
resolvers += Resolver.bintrayRepo("wrotki", "maven-repo")
resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"

enablePlugins(ScalaJSPlugin)
enablePlugins(ScalaJSBundlerPlugin)

scalaJSUseMainModuleInitializer := true
scalacOptions ++= Seq("-Ypartial-unification"
//  , "-Xprint:typer"
)

npmDependencies in Compile += "aws-sdk" -> "2.315.0"
//jsDependencies += ProvidedJS / "glob.js"

val circeVersion = "0.9.3"

libraryDependencies ++= Seq(
  "io.circe" %%% "circe-core",
  "io.circe" %%% "circe-generic",
  "io.circe" %%% "circe-parser"
).map(_ % circeVersion)

libraryDependencies ++= Seq(
  "io.wrotki" %%% "scala-js-awssdk" % "1.0.2", 
  "com.lihaoyi" %%% "utest" % "0.6.5" % "test",
   "org.typelevel" %%% "cats-core" % "1.4.0",
  "org.typelevel" %%% "cats-effect" % "1.0.0",
  // https://mvnrepository.com/artifact/com.propensive?p=2
//  "com.propensive" %%% "rapture-io" % "2.0.0-M9",
//  "com.propensive" %%% "rapture-net" % "2.0.0-M9",
//  "com.propensive" %%% "rapture-uri" % "2.0.0-M9",
//  "com.propensive" %%% "rapture-xml" % "2.0.0-M9",
//  "com.propensive" %%% "rapture-http" % "2.0.0-M9",
  // https://www.reddit.com/r/scala/comments/8spei9/recommended_scalajs_http_clients/
  // https://github.com/softwaremill/sttp
//  "com.softwaremill.sttp" %%% "core_2.12" % "1.3.9",
  "fr.hmil" %%% "roshttp" % "2.1.0",
  "io.monix" %%% "monix" % "2.3.0",
  // https://github.com/scalajs-io/nodejs
  "io.scalajs" %%% "nodejs" % "0.4.2",
  // https://github.com/scalajs-io/xml2js
  "io.scalajs.npm" %%% "xml2js" % "0.4.2"
)

testFrameworks += new TestFramework("utest.runner.Framework")

version := Versions.app
crossScalaVersions := Versions.scalaVersions
shellPrompt := { state => s"[${Project.extract(state).currentProject.id}] $$ " }
resolvers += Resolver.jcenterRepo
resolvers += Resolver.bintrayRepo("wrotki", "maven-repo")
organization := "io.wrotki"
homepage := Some(url("https://github.com/wrotki/smoothaws"))
scmInfo := Some(ScmInfo(
  url("https://github.com/wrotki/smoothaws"),
  "scm:git:git@github.com:wrotki/smoothaws.git",
  Some("scm:git:git@github.com:wrotki/smoothaws.git")
))
bintrayOrganization := Some("wrotki")
bintrayPackageLabels := Seq("scala", "scala.js")
bintrayPackage := "smoothaws"
bintrayRepository := "maven-repo"
bintrayVcsUrl := Some("git:git@github.com:wrotki/smoothaws.git")
publishMavenStyle := true
licenses += ("MIT", url("http://opensource.org/licenses/MIT"))
//libraryDependencies ++= Seq("org.scala-js" %%% "scalajs-dom" % "0.9.2", "io.wrotki" %%% "scala-js-node" % "1.0.2")
scalaJSStage in Global := FastOptStage
