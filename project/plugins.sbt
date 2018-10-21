scalacOptions ++= Seq( "-unchecked", "-deprecation" )

resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"
resolvers += Resolver.jcenterRepo

addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.25")

addSbtPlugin("ch.epfl.scala" % "sbt-scalajs-bundler" % "0.13.1")

addSbtPlugin("org.foundweekends" % "sbt-bintray" % "0.5.4")

// Dependency Resolution
addSbtPlugin("io.get-coursier" % "sbt-coursier" % "1.0.3")

//// Publishing
//addSbtPlugin("me.lessis" % "bintray-sbt" % "0.3.0")

// http://szimano.org/automatic-deployments-to-jfrog-oss-and-bintrayjcentermaven-central-via-travis-ci-from-sbt/
// https://groups.google.com/forum/#!topic/scala-js/4GiIOW3r-8o
// https://contributors.scala-lang.org/t/alternative-scalajs-scala-native-distribution-mechanisms/1166/9
// https://github.com/sbt/sbt-bintray/issues/55
// https://github.com/sbt/sbt-bintray
