package main

import aws.Invoker

import scala.scalajs.js.annotation._
import cats.effect._
import cats.syntax.all._
import particles.AWS.Batch

// https://typelevel.org/cats/
// https://typelevel.org/cats-effect/datatypes/io.html#introduction
// https://stackoverflow.com/questions/42998773/scala-js-pass-command-line-arguments-from-sbt-run

object Main extends IOApp {

  def run(args: List[String]): IO[ExitCode] =
    args.headOption match {
      case Some(name) =>
        IO(println(s"Hello, $name.")).as(ExitCode.Success)
      case None =>
//        IO(System.err.println("Usage: MyApp name")).as(ExitCode(2))
//        IO(System.err.println("DONE. No args")).as(ExitCode.Success)
        doRosHTTPSomething
        println("invoke batchRegisterJobDefIfMissing")
        Batch.registerJobDefIfMissing("ParticleBatchRegisterJobDefinitionTest").as(ExitCode.Success)
//        IO(essay).as(ExitCode.Success)
    }

  def doRaptureSomething: Unit = {
    import rapture._
    import core._
    import io._
    import net._
//    import http._
    import uri._
//    import json._
    import codec._
    import encodings.system._
//    import jsonBackends.jackson._
//    import jsonInterop._
//    import formatters.humanReadable._
    val index = uri"https://mirrors.polyverse.io/fedora/updates/23/x86_64/repodata/repomd.xml"
    println(index)
  }

  def doSttpSomething: Unit = {
    // https://www.reddit.com/r/scala/comments/8spei9/recommended_scalajs_http_clients/

  }

  def doRosHTTPSomething: Unit = {
    // https://github.com/hmil/RosHTTP
    import fr.hmil.roshttp.HttpRequest
    import monix.execution.Scheduler.Implicits.global
    import scala.util.{Failure, Success}
    import fr.hmil.roshttp.response.SimpleHttpResponse

    // Runs consistently on the jvm, in node.js and in the browser!
    val request = HttpRequest("https://schema.org/WebPage")

    request.send().onComplete({
      case res:Success[SimpleHttpResponse] => println(res.get.body)
      case e: Failure[SimpleHttpResponse] => println("Houston, we got a problem!")
    })
  }
}