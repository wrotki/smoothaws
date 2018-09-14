package org.company.lib

import scala.scalajs.js.annotation.JSExportTopLevel

object Lib {

  @JSExportTopLevel("org.company.lib.Lib")
  def function(): Unit = {
    println("Hello world!")
  }

}
