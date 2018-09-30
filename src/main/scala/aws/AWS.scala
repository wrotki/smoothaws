package aws

import com.definitelyscala.awssdk.Batch.{Batch, DescribeJobDefinitionsParam, JobDefinitionContainerProperties, RegisterJobDefinitionParam}
import com.definitelyscala.awssdk.ClientConfig
import com.definitelyscala.awssdk.DynamoDB._
import interop.Glob

import scala.scalajs.js
import scala.scalajs.js.{Dictionary, JSON}
import scala.scalajs.js.annotation.JSImport


@js.native
@JSImport("aws-sdk", JSImport.Default)
object AWS extends js.Object {
  var config: ClientConfig = js.native
}


@js.native
@js.annotation.JSGlobal("$g")
object Window extends js.Object

object Invoker {
  AWS.config.region = "us-west-2"

  val cli = new Batch

  def stgfy(v: js.Any): String = {

    val ret = JSON.stringify(v, space = " ")
    ret
  }

  def doSomething: Unit = {

    // println(Glob.getGlobalVariables(AWS) map println)

    //    val config = js.Dynamic.literal(
    //      region = "us-west-2"
    //    ).asInstanceOf[ClientConfig]
    println(JSON.stringify(AWS, space = " "))


    val cli = new DocumentClient
    val next: js.Function2[js.Any, js.Any, Unit] = { (x: js.Any, y: js.Any) =>
      val strg = stgfy(y)
      println(s"I'm back from scanning data, err: $x, data: $strg")
    }

    // js.Dynamic.literal(foo = 42, bar = "foobar")
    val scanParam = js.Dynamic.literal(
      TableName = "BigBang.staging.InstanceTypes"
    ).asInstanceOf[ScanParam]

    cli.scan(scanParam, next)
  }

  def doBatch: Unit = {
    println(JSON.stringify(cli, space = " "))

    val djdnParam = js.Dynamic.literal(
      //      status = "ACTIVE"
    ).asInstanceOf[DescribeJobDefinitionsParam]

    val next: js.Function2[js.Any, js.Any, Unit] = { (x: js.Any, y: js.Any) =>
      val strg = stgfy(y)
      //      println(s"I'm back from describing jobdefs, err: $x, data: $strg")
      val dataDict: Dictionary[js.Any] = y.asInstanceOf[js.Dictionary[js.Any]]
      val gv: PartialFunction[(String, js.Any), js.Any] = {
        case (k: String, v: js.Any) => v
      }
      val res = dataDict map {
        gv andThen stgfy
      }
      println(res)
    }

    cli.describeJobDefinitions(djdnParam, next)
  }

  def batchRegisterJobDef: Unit = {

    val containerProperties = js.Dynamic.literal(
      command = js.Array("foo"),
      image = "Van Klomp",
      memory = 1024,
      vcpus = 1.0
    ).asInstanceOf[JobDefinitionContainerProperties]
    val rjdnParam = js.Dynamic.literal(
      `type` = "container",
      jobDefinitionName = "ParticleBatchRegisterJobDefinitionTest",
      containerProperties = containerProperties
    ).asInstanceOf[RegisterJobDefinitionParam]

    val next: js.Function2[js.Any, js.Any, Unit] = { (x: js.Any, y: js.Any) =>
      val strg = stgfy(y)
      val error = stgfy(x)
      println("Result: "+strg)
      println("Error: "+x)
    }

    cli.registerJobDefinition(rjdnParam, next)
  }

}
