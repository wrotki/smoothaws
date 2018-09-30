package com.definitelyscala.awssdk.Batch

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport


@js.native //@js.annotation.JSGlobal("AWS.Batch")
@JSImport("aws-sdk", "Batch")
class Batch protected () extends js.Object {
  def this(options: js.Any = js.native) = this()
  def describeJobDefinitions(params: DescribeJobDefinitionsParam, next: js.Function2[js.Any, js.Any, Unit]): Unit = js.native
  def registerJobDefinition(params: RegisterJobDefinitionParam, next: js.Function2[js.Any, js.Any, Unit]): Unit = js.native
}
