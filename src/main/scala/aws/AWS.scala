package aws

import com.definitelyscala.awssdk.DynamoDB._
import interop.Glob

import scala.scalajs.js
import scala.scalajs.js.JSON
import scala.scalajs.js.annotation.JSImport


@js.native
@JSImport("aws-sdk", JSImport.Default)
object AWS extends js.Object

@js.native
@js.annotation.JSGlobal("$g")
object Window extends js.Object

object Invoker{

  def doSomething = {
    //val cli = new DocumentClient

    val next: js.Function2[Any, Any, Unit] = { (x: Any, y: Any) => println("I'm back") }

    // js.Dynamic.literal(foo = 42, bar = "foobar")
    val scanParam = js.Dynamic.literal().asInstanceOf[ScanParam]

    // cli.scan(scanParam, next)
    println(JSON.stringify(AWS, space=" "))
    println(Glob.getGlobalVariables(AWS) map println)
  }

}
