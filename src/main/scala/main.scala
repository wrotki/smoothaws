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
import io.scalajs.nodejs.zlib.Zlib
import monix.execution.CancelableFuture
import particles.AWS.Batch
import rpm.Index

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
//          _ <- IO(Index.fetch)
          _ <- IO(Index.fetchObservable)
          last <- Batch.registerJobDefIfMissing("ParticleBatchRegisterJobDefinitionTest").as(ExitCode.Success)
        } yield last
        result
//        IO(essay).as(ExitCode.Success)
    }

}