package interop

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

@JSName("Glob")
@js.native
object Glob extends js.Object {
  def foo(): String = js.native
  def getOpts(): js.Object = js.native
  def encKey(): js.Object = js.native
  def immutableTest(): js.Object = js.native
  def getGlobalVariables(obj: js.Object): js.Array[String] = js.native
}
