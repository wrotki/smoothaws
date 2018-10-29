package main

import java.nio.ByteBuffer

import aws.Invoker
import aws.Invoker.stgfy

import scala.scalajs.js.annotation._
import cats.effect._
import cats.syntax.all._
import fr.hmil.roshttp.BackendConfig
import fr.hmil.roshttp.node.buffer.Buffer
import fr.hmil.roshttp.response.StreamHttpResponse
import monix.execution.CancelableFuture
import particles.AWS.Batch

import scala.scalajs.js
import scala.scalajs.js.typedarray.Int8Array

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
        println("invoke batchRegisterJobDefIfMissing")
        val result = for {
          _ <- IO(doRosHTTPSomething)
          last <- Batch.registerJobDefIfMissing("ParticleBatchRegisterJobDefinitionTest").as(ExitCode.Success)
        } yield last
        result
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

  import io.scalajs.nodejs.buffer._

  def doRosHTTPSomething: Unit = {
    // https://github.com/hmil/RosHTTP
    import fr.hmil.roshttp.HttpRequest
    import monix.execution.Scheduler.Implicits.global
    import scala.util.{Failure, Success}
    import fr.hmil.roshttp.response.SimpleHttpResponse

    // Runs consistently on the jvm, in node.js and in the browser!
    val request = HttpRequest("https://archives.fedoraproject.org/pub/archive/fedora/linux/releases/23/Everything/x86_64/os/repodata/0fa09bb5f82e4a04890b91255f4b34360e38ede964fe8328f7377e36f06bad27-primary.xml.gz")
      .withBackendConfig(BackendConfig(maxChunkSize = 1024*1024, internalBufferLength = 1024*1024))

//    request.send().onComplete({
//      case res:Success[SimpleHttpResponse] => gunzipBody(res.get.body)
//      case e: Failure[SimpleHttpResponse] => println(s"Houston, we got a problem!: $e")
//    })
    var index = Buffer.alloc(0)
    val streamFuture = request.stream().map({ r: StreamHttpResponse =>
      println(s"StreamHttpResponse received, status code: ${r.statusCode}")
//      println(s"StreamHttpResponse received, body: ${r.body}")
      val bufFut: CancelableFuture[Unit] = r.body.foreach{ (buffer: ByteBuffer) =>
        println(s"Stream chunk received, buffer isDirect: ${buffer.isDirect}")
        println(s"Stream chunk received, buffer limit: ${buffer.limit}")
        println(s"Stream chunk received, buffer position: ${buffer.position}")
        println(s"Stream chunk received, buffer remaining: ${buffer.remaining}")
        println(s"Stream chunk received, buffer: ${buffer}")
        val len = buffer.remaining
        println(s"Stream chunk received, length: ${len}")
        val arr = new Int8Array(buffer.limit)
        var i = 0
        while (i < arr.length) {
         arr(i) = buffer.get(i)
          i += 1
        }
        val chunk = js.Dynamic.newInstance(js.Dynamic.global.Buffer)(arr).asInstanceOf[Buffer]
        index = Buffer.concat(js.Array(index,chunk))
      }
      bufFut onComplete {
        case Success(_) => {
          println("bufFut complete")
          gunzipBody(index)
        }
        case e => {
          println(s"bufFut failed: $e, stack:")
          e.toEither.left.get.printStackTrace()
        }
      }
    })
    streamFuture onComplete { _ =>
      println("Stream future complete")
      //gunzipBody(index)
    }
  }

  def gunzipBody(body: Buffer): Unit = {
    import io.scalajs.nodejs.zlib._
    //val buf = Buffer.from(body)

    val next: js.Function2[js.Any, js.Any, Unit] = { (x: js.Any, y: js.Any) =>
      val strg = y.asInstanceOf[Buffer]
      val error = stgfy(x)
      println("Result: " + strg)
      println("Error: " + x)
      x match {
        case null => println(strg.toLocaleString())
        case e => println(s"Error: $error")
      }
    }
    Zlib.gunzip(body, next)
  }
}