package test.utest.examples.examples

import utest._
import org.company.lib.Lib

object HelloTests extends TestSuite{
  val tests = this{
    'test1{
      Lib.function()
    }
    'test2{
      1
    }
    'test3{
      val a = List[Byte](1, 2)
      //a(10)
    }
  }
}
