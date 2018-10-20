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
        println("invoke batchRegisterJobDefIfMissing")
        Batch.registerJobDefIfMissing("ParticleBatchRegisterJobDefinitionTest").as(ExitCode.Success)
//        IO(essay).as(ExitCode.Success)
    }
}