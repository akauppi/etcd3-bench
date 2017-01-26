package test

import com.coreos.jetcd.api.{DeleteRangeRequest, DeleteRangeResponse, PutResponse, RangeResponse}
import com.whisk.docker.impl.dockerjava.DockerKitDockerJava
import com.whisk.docker.scalatest.DockerTestKit
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{FlatSpec, Matchers}
import test.tools.DockerEtcdV3
import com.coreos.jetcd.{EtcdClient, EtcdClientBuilder, EtcdKV}
import com.google.common.util.concurrent.ListenableFuture
import com.google.protobuf.ByteString
import scala.concurrent.duration._

import scala.languageFeature.postfixOps

class EtcdV3Test extends FlatSpec with Matchers with ScalaFutures with DockerEtcdV3 with DockerTestKit with DockerKitDockerJava {
  import EtcdV3Test._

  behavior of "EtcdV3Test"

  implicit val pc = PatienceConfig(20 seconds, 500 milli)

  /*** disabled
  it should "be able to launch etcd-v3 within Docker" in {
    //dockerContainers.map(_.image).foreach(println)
    //dockerContainers.forall(_.isReady().futureValue) shouldBe true

    whenReady(etcdV3Container.getPorts()) { ports =>
      info( s"etcdV3 using ports: $ports" )

      assert(ports.size == 1)
      port = ports(2379)    // random host side port (e.g. 32774)
    }
  }
  ***/

  val port: Int = etcdV3Container.getPorts.map { ports =>
    info( s"etcdV3 using ports: $ports" )

    assert(ports.size == 1)
    ports(2379)    // random host side port (e.g. 32774)
  }.futureValue

  it should "be able to write/read key-values" in {
    val (testKey,testValue) = ("testKey", "testValue")

    val client: EtcdClient = EtcdClientBuilder.newBuilder().endpoints(s"http://$dockerIP:$port").build()
    val kvClient: EtcdKV = client.getKVClient

    val bsk: ByteString = ByteString.copyFrom(testKey, "UTF-8")
    val bsv: ByteString = ByteString.copyFrom(testValue, "UTF-8")

    // put the key-value
    val putFut: ListenableFuture[PutResponse] = kvClient.put(bsk, bsv)
    putFut.get()  // wait

    // get the value
    val lf: ListenableFuture[RangeResponse]= kvClient.get(bsk)
    val rr: RangeResponse = lf.get()

    rr.getKvsCount shouldBe 1
    rr.getKvs(0).getValue.toStringUtf8 shouldBe testValue

    // delete the key
    val deleteFuture: ListenableFuture[DeleteRangeResponse] = kvClient.delete(bsk)
    deleteFuture.get()  // wait
  }

  // tbd. more tests
}

object EtcdV3Test {

  /*
  * Provide the IP to reach the docker
  *
  * tbd. Check if 'docker-it-scala' were to provide this to us, in some way.
  */
  lazy val dockerIP: String = {

    // tbd. read it from 'DOCKER_HOST' env.var
    "192.168.99.100"
  }

  /*** disabled
  implicit def dur2span(dur: FiniteDuration): Span = {
    Span(dur.toMillis, Milliseconds)
  }
  ***/
}
