package rpm

import java.nio.ByteBuffer

import cats.effect.IO
import cats.effect._
import IO._
import cats.syntax.all._

import fr.hmil.roshttp.response.StreamHttpResponse
import fr.hmil.roshttp.{BackendConfig, HttpRequest}
import io.scalajs.nodejs.buffer._
import io.scalajs.nodejs.zlib.Zlib
import monix.eval.{Callback, Task}
import monix.execution.Scheduler.Implicits.global
import monix.execution.cancelables.SingleAssignmentCancelable
import monix.execution.{Ack, Cancelable, CancelableFuture}
import monix.reactive.OverflowStrategy.Unbounded
import monix.reactive._

import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.typedarray.Int8Array
import scala.util.{Failure, Success}

@js.native
trait MyObject extends js.Object {
  var root: String = js.native
}

object Index {

  def testObservableFold: Unit = {
    val ob = Observable("a", "b", "c")
    val xmlt: Task[String] = ob.foldLeftL("")(concater)// (_ + _)
    val cancelable = xmlt.runOnComplete { result =>
      result match {
        case Success(value) => {
          1 to 100 foreach { _ => println("SUCCESS") }
          println(s"SUCCESS: '${value.take(20)}'")
          println(value.take(20))
        }
        case Failure(ex) =>
          1 to 100 foreach { _ => println(s"ERROR: ${ex.getMessage}") }
      }
    }
  }

  def fetchObservable: IO[String] = {
    val o: Observable[String] = indexXml map {
      _.toLocaleString
    }
    //    o.foreach { s => println(s.take(100)) }
    //    val pull = xmlt.runAsync.foreach(xmlToIndexObject(_))
    //    val pull = xmlt.runAsync.foreach(s => println(s"Length: ${s.length}"))
    val xmlt: Task[String] = o.foldLeftL("")(concater)// (_ + _)
//    val cancelable = xmlt.runOnComplete { result =>
//      result match {
//        case Success(value) => {
//          1 to 100 foreach { _ => println("SUCCESS") }
//          println(s"SUCCESS: '${value.take(2000)}'")
//        }
//        case Failure(ex) => {
//          val msg = ex.getMessage
//          1 to 100 foreach { _ => println(s"ERROR: ${msg}") }
//        }
//      }
//    }
    val result: CancelableFuture[String] = xmlt.runAsync
    val io: IO[String] = IO.fromFuture(IO(result))
//    xmlt.runAsync {
//      new Callback[String] {
//        def onSuccess(value: String): Unit = {
//          println("SUCCESS")
//          println(value.take(256))
//        }
//
//        def onError(ex: Throwable): Unit =
//          println(s"ERROR: ${ex.getMessage}")
//      }
//    }
//    val tryingNow = xmlt.coeval
//    // tryingNow: Coeval[Either[CancelableFuture[String],String]] = ???
//
//    tryingNow.value match {
//      case Left(future) =>
//        // No luck, this Task really wants async execution
//        future.foreach(r => println(s"Async: $r"))
//      case Right(result) =>
//        println(s"Got lucky: $result")
//    }
    io
  }

  def concater(a: String ,b: String): String = {
//    println(s"Prev length: ${a.length}")
    println(s"Append: ${b.take(20)}")
    a + b
  }

  def xmlToIndexObject(input: String): Unit = {
    import io.scalajs.JSON
    import io.scalajs.npm.xml2js._
    import scalajs.js

    val head = input.take(200)
    println(head)
    val xml = "<root>Hello xml2js!</root>"
    Xml2js.parseString[MyObject](xml, (err, result) => {
      println(s"Error: $err")
      println(s"Result: $result")
      if (err != null) {
        println(s"Error: $err")
      } else {
        println("results: " + JSON.stringify(result)) // results: {"root":"Hello xml2js!"}
        println(result.root) // Hello xml2js!
      }
    })

  }

  def indexXml: Observable[Buffer] = {
    // https://monix.io/docs/3x/reactive/javascript.html
    Observable.create[Buffer](Unbounded) { subscriber =>
      val c = SingleAssignmentCancelable()
      // Forced conversion, otherwise canceling will not work!
      val f: scalajs.js.Function1[Buffer, Ack] = (b: Buffer) =>
        subscriber.onNext(b).syncOnStopOrFailure(_ => c.cancel())
      // target.addEventListener(event, f)
      val complete = () => subscriber.onComplete()
      fetch(f, complete)
      c := Cancelable(() => ())
    }
  }

  def fetch(f: scalajs.js.Function1[Buffer, Ack], complete: scalajs.js.Function): Unit = {
    // https://github.com/hmil/RosHTTP
    // Runs consistently on the jvm, in node.js and in the browser!
    val request = HttpRequest("https://archives.fedoraproject.org/pub/archive/fedora/linux/releases/23/Everything/x86_64/os/repodata/0fa09bb5f82e4a04890b91255f4b34360e38ede964fe8328f7377e36f06bad27-primary.xml.gz")
      .withBackendConfig(BackendConfig(maxChunkSize = 1024, internalBufferLength = 128 * 1024 * 1024))

    def onData: js.Function = {
      (x: js.Any, y: js.Any) =>
        val buf = x.asInstanceOf[Buffer]
        f(buf)
    }

    val gunzip = Zlib.createGunzip()
    gunzip.on(eventName = "data", listener = onData)
    gunzip.on(eventName = "end", listener = complete)

    val streamFuture: Future[Unit] = request.stream().map({ r: StreamHttpResponse =>
      val bufFut: CancelableFuture[Unit] = r.body
        .doOnComplete { () =>
          println("End of input stream encountered")
          gunzip.end()
        }
        .foreach { buffer: ByteBuffer =>
        val chunk = nioToNodeBuffer(buffer)
        gunzip.write(chunk)
      }
    })
  }

  def nioToNodeBuffer(buffer: ByteBuffer): Buffer = {
    val len = buffer.remaining
    val arr = new Int8Array(buffer.limit)
    var i = 0
    while (i < arr.length) {
      arr(i) = buffer.get(i)
      i += 1
    }
    js.Dynamic.newInstance(js.Dynamic.global.Buffer)(arr).asInstanceOf[Buffer]
  }

}