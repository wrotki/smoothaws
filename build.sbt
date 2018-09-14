name := "Scala Hello World"

scalaVersion := "2.12.6" // or any other Scala version >= 2.10.2

enablePlugins(ScalaJSPlugin)

enablePlugins(ScalaJSBundlerPlugin)

scalaJSUseMainModuleInitializer := true

npmDependencies in Compile += "aws-sdk" -> "2.315.0"

libraryDependencies += "com.lihaoyi" %%% "utest" % "0.4.8" % "test"

testFrameworks += new TestFramework("utest.runner.Framework")

