package com.definitelyscala.awssdk.Batch

import scala.scalajs.js

@js.native
trait JobDefinitionContainerProperties extends js.Object {
  var command: js.Array[String] = js.native
  var image: String = js.native
  var memory: Long = js.native
  var vcpus: Double = js.native
}
