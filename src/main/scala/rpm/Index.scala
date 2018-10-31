package rpm

import java.nio.ByteBuffer

import aws.Invoker.stgfy
import fr.hmil.roshttp.response.StreamHttpResponse
import fr.hmil.roshttp.{BackendConfig, HttpRequest}
import io.scalajs.nodejs.buffer._
import io.scalajs.nodejs.zlib.Zlib
import monix.execution.{Ack, Cancelable, CancelableFuture}
import monix.execution.Scheduler.Implicits.global
import monix.execution.cancelables.SingleAssignmentCancelable
import monix.reactive.OverflowStrategy.Unbounded
import monix.reactive._
import monix.reactive.observers.Subscriber

import concurrent.duration._
import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.typedarray.Int8Array

object Index {

  def fetch(f: scalajs.js.Function1[Buffer, Ack]): Unit = {
    // https://github.com/hmil/RosHTTP
    // Runs consistently on the jvm, in node.js and in the browser!
    val request = HttpRequest("https://archives.fedoraproject.org/pub/archive/fedora/linux/releases/23/Everything/x86_64/os/repodata/0fa09bb5f82e4a04890b91255f4b34360e38ede964fe8328f7377e36f06bad27-primary.xml.gz")
      .withBackendConfig(BackendConfig(maxChunkSize = 1024, internalBufferLength = 1024*1024))

    def onData: js.Function = {
      (x: js.Any, y: js.Any) =>
        val buf = x.asInstanceOf[Buffer]
        f(buf)
    }
    val gunzip = Zlib.createGunzip()
    gunzip.on(eventName = "data", listener = onData)

    val streamFuture: Future[Unit] = request.stream().map({ r: StreamHttpResponse =>
      val bufFut: CancelableFuture[Unit] = r.body.foreach{ buffer: ByteBuffer =>
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

  def fetchObservable: Unit = {
    val o = indexXml.foreach{ b =>
      println(b.toLocaleString)
    }
  }

  def indexXml: Observable[Buffer] = {
    // https://monix.io/docs/3x/reactive/javascript.html
    Observable.create[Buffer](Unbounded) { subscriber =>
      val c = SingleAssignmentCancelable()
      // Forced conversion, otherwise canceling will not work!
      val f: scalajs.js.Function1[Buffer, Ack] = (b: Buffer) =>
        subscriber.onNext(b).syncOnStopOrFailure(_ => c.cancel())
      // target.addEventListener(event, f)
      fetch(f)
      c := Cancelable(() => () )
    }
  }
}