package test

import com.coreos.jetcd.api.{DeleteRangeRequest, DeleteRangeResponse, PutResponse, RangeResponse}
import com.whisk.docker.impl.dockerjava.DockerKitDockerJava
import com.whisk.docker.scalatest.DockerTestKit
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}
import test.tools.DockerEtcdV3
import com.coreos.jetcd.{EtcdClient, EtcdClientBuilder, EtcdKV}
import com.google.common.util.concurrent.{Futures, ListenableFuture}
import com.google.protobuf.ByteString
import com.google.common.util.concurrent.FutureCallback

import scala.concurrent.{Future, Promise}
import scala.concurrent.duration._
import scala.languageFeature.postfixOps

class EtcdV3Test extends FlatSpec with Matchers with ScalaFutures
  with DockerEtcdV3 with DockerTestKit with DockerKitDockerJava {
  import EtcdV3Test._

  lazy val kvClient: EtcdKV = {
    val port: Int = etcdV3Container.getPorts.map { ports =>
      info( s"etcdV3 using ports: $ports" )

      assert(ports.size == 1)
      ports(2379)    // random host side port (e.g. 32774)
    }.futureValue

    val ip = dockerIP
    //info( s"Reaching for etcd at http://$ip:$port" )

    val client: EtcdClient = EtcdClientBuilder.newBuilder()
      .endpoints(s"http://$ip:$port")
      .build()
    client.getKVClient
  }

  behavior of "Etcd v3 API via 'jetcd'"

  implicit val pc = PatienceConfig(20 seconds, 500 milli)

  it should "be able to write, read and delete a key/value pair" in {
    val (testKey,testValue) = ("tk", "tv")

    val bsk: ByteString = ByteString.copyFrom(testKey, "UTF-8")
    val bsv: ByteString = ByteString.copyFrom(testValue, "UTF-8")

    // put the key-value
    val putFut: Future[PutResponse] = kvClient.put(bsk, bsv).asScala

    whenReady(putFut) { resp =>
      // get the value
      val fut2: Future[RangeResponse]= kvClient.get(bsk).asScala
      whenReady(fut2) { (rr: RangeResponse) =>
        rr.getKvsCount shouldBe 1
        rr.getKvs(0).getValue.toStringUtf8 shouldBe testValue

        val rev: Long = rr.getHeader.getRevision
        info( s"Revision: $rev")  // 2

        // delete the key
        val delFut: Future[DeleteRangeResponse] = kvClient.delete(bsk).asScala
        whenReady(delFut) { _ =>

          val fut3: Future[RangeResponse]= kvClient.get(bsk).asScala
          whenReady(fut3) { (rr: RangeResponse) =>
            rr.getKvsCount shouldBe 0   // not found any more
          }
        }
      }
    }
  }

  // tbd. more tests
}

object EtcdV3Test {
  /*
  * Provide the IP to reach the docker
  *
  * tbd. Maybe there is a way 'docker-it-scala' can provide this?
  */
  lazy val dockerIP: String = {
    val Rtmp = """.+?://([0-9]+\.[0-9]+\.[0-9]+\.[0-9]+):.+""".r

    sys.env.get("DOCKER_HOST") match {    // e.g. "tcp://192.168.99.100:2376"
      case Some(Rtmp(ip)) => ip
      case x => throw new RuntimeException( s"Unexpected 'DOCKER_HOST': ${x.getOrElse("None")}" )
    }
  }

  /*
  * From http://stackoverflow.com/questions/18026601/listenablefuture-to-scala-future
  */
  implicit class RichListenableFuture[T](lf: ListenableFuture[T]) {
    def asScala: Future[T] = {
      val p = Promise[T]()
      Futures.addCallback(lf, new FutureCallback[T] {
        def onSuccess(result: T): Unit    = p.success(result)
        def onFailure(t: Throwable): Unit = p.failure(t)
      })
      p.future
    }
  }
}
