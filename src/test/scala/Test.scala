package test.utest.examples.examples

import java.io.File

import cats.effect._
import cats.syntax.all._
import utest._
import org.company.lib.Lib
import particles.AWS.Batch


object HelloTests extends TestSuite {
  val tests = this {
    'test1 {
      Lib.function()
    }
    'test2 {
      1
    }
    'test3 {
      val a = List[Byte](1, 2)
      //a(10)
    }
    'testBatchJobDefUpsert {

      // https://www.beyondthelines.net/computing/scala-future-and-execution-context/
      implicit val executor = scala.concurrent.ExecutionContext.global

      val res = Batch.registerJobDefIfMissing("ParticleBatchRegisterJobDefinitionTest")
      res.unsafeRunAsync(
        r => {
          println(s"Batch.registerJobDefIfMissing complete: $r")
        }
      )
      res.unsafeToFuture()
    }
  }
}
