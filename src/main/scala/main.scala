package main

import aws.Invoker

import scala.scalajs.js.annotation._

object Main {

  @JSExportTopLevel("org.company.HelloWorld")
  def main(args: Array[String]): Unit = {
    println("Hello beautiful world!")

//    Invoker.doSomething
//    Invoker.doBatch
    Invoker.batchRegisterJobDef
  }
}