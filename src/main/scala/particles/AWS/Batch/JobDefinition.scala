package particles.AWS.Batch

import aws.Invoker.{cli, stgfy}
import cats.effect.IO
import com.definitelyscala.awssdk.Batch.{DescribeJobDefinitionsParam, JobDefinitionContainerProperties, RegisterJobDefinitionParam}

import scala.scalajs.js
import js.JSConverters._
import scala.collection.mutable
import scala.scalajs.js.typedarray.ArrayBuffer
import scala.scalajs.js.{Dictionary, JSON}

sealed trait BatchProps

case class JobDefinitionsList(
                               jobDefinitions: List[JobDefinition]
                             ) extends BatchProps

case class Volume(
                   name: String /*,
                   host: Host
                   */
                 ) extends BatchProps

case class ContainerProperties(
                                image: String,
                                command: List[String],
                                memory: Int,
                                vcpus: Double
                              ) extends BatchProps

case class JobDefinition(
                          jobDefinitionName: String,
                          jobDefinitionArn: String,
                          revision: Int,
                          status: String,
                          `type`: String,
                          containerProperties: ContainerProperties
                        ) extends BatchProps {
  def cliParams: RegisterJobDefinitionParam = {
    val containerProperties = js.Dynamic.literal(
      command = this.containerProperties.command.toJSArray,
      image = this.containerProperties.image,
      memory = this.containerProperties.memory,
      vcpus = this.containerProperties.vcpus
    ).asInstanceOf[JobDefinitionContainerProperties]
    js.Dynamic.literal(
      `type` = this.`type`,
      jobDefinitionName = this.jobDefinitionName,
      containerProperties = containerProperties
    ).asInstanceOf[RegisterJobDefinitionParam]
  }

  def make: (Either[Throwable, String] => Unit) => Unit = {
    cb => {
      val next: js.Function2[js.Any, js.Any, Unit] = { (x: js.Any, y: js.Any) =>
        val strg = stgfy(y)
        val error = stgfy(x)
        println("Result: " + strg)
        println("Error: " + x)
        x match {
          case null => cb(Right(strg))
          case e => cb(Left(new Throwable(error)))
        }
      }
      cli.registerJobDefinition(cliParams, next)
    }
  }
}

object JobDefinitions {
  def list: IO[Seq[JobDefinition]] = {
    IO.async {
      describeJobDefinitions
    }
  }

  import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._
  //  import io.circe.generic.semiauto.deriveDecoder
  import io.circe.generic.auto._, io.circe.syntax._
  import io.circe.generic.JsonCodec, io.circe.syntax._
  import cats.syntax.show._

  // https://stackoverflow.com/questions/37920023/could-not-find-implicit-value-for-evidence-parameter-of-type-org-apache-flink-ap
  // implicit val typeInfo = TypeInformation.of(classOf[(Int, String)]) // Doesn't work

  // https://stackoverflow.com/questions/50477920/dynamic-json-decoding-with-circe
  // https://medium.com/@djoepramono/how-to-parse-json-in-scala-c024cb44f66b



  private def parseJSON(json: String): List[JobDefinition] = {
//    println(s"JSON to parse: $json")

    val result = parse(json).flatMap(_.as[JobDefinitionsList])//.leftMap(_.show)
    result match {
      case r: Right[_, _] => {
        val head = r.right.get.jobDefinitions.head
//        println(s"Success: $head")
        r.right.get.jobDefinitions
      }
      case r: Left[lt, rt] => {
        println(s"Error: $r")
        List[JobDefinition]()
      }
    }
  }

  private def describeJobDefinitions: (Either[Throwable, Seq[JobDefinition]] => Unit) => Unit = {
    cb => {
      //      println(JSON.stringify(cli, space = " "))

      val djdnParam = js.Dynamic.literal(
        //      status = "ACTIVE"
      ).asInstanceOf[DescribeJobDefinitionsParam]

      val next: js.Function2[js.Any, js.Any, Unit] = { (x: js.Any, y: js.Any) =>
        val strg = stgfy(y)
        //      println(s"I'm back from describing jobdefs, err: $x, data: $strg")
        //        val dataDict: Dictionary[js.Any] = y.asInstanceOf[js.Dictionary[js.Any]]
        //        val gv: PartialFunction[(String, js.Any), js.Any] = {
        //          case (k: String, v: js.Any) => v
        //        }
        //        val res: mutable.Iterable[String] = dataDict map {
        //          gv andThen stgfy
        //        }
        //println(res)
        x match {
          case null =>
            //            val first: (String, js.Any) = dataDict.head
            //            printf("First: %s\n", first._2)
            cb(Right(parseJSON(strg)))
//            cb(Right(Seq( // TODO do proper JSON deserialization
//
//              //              JobDefinition("" /*strg*/ ,
//              //                containerProperties = ContainerProperties(
//              //                  image = "VanKlomp",
//              //                  command = Seq("bash", "-c", "sleep 10"),
//              //                  memory=1024,
//              //                  vcpus=1.0
//              //                )
//              //              )
//            ))) //TODO: deserialize
          case e => cb(Left(new Throwable(e.toString)))
        }
      }
      cli.describeJobDefinitions(djdnParam, next)
    }
  }
}