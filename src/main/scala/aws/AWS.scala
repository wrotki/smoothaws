package aws

import com.definitelyscala.awssdk.Batch.Batch
import com.definitelyscala.awssdk.ClientConfig
import com.definitelyscala.awssdk.DynamoDB._
import interop.Glob

import scala.scalajs.js
import scala.scalajs.js.{Any => JSAny}
import scala.scalajs.js.JSON
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

  def stgfy(v: JSAny): String = {

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
    val next: js.Function2[JSAny, JSAny, Unit] = { (x: JSAny, y: JSAny) =>
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
    val cli = new Batch
    println(JSON.stringify(cli, space = " "))
  }
}
