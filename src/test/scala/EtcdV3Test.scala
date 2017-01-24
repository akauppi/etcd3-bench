package test

import com.whisk.docker.impl.dockerjava.DockerKitDockerJava
import com.whisk.docker.scalatest.DockerTestKit
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{FlatSpec, Matchers}
import test.tools.DockerEtcdV3

import scala.languageFeature.postfixOps

class EtcdV3Test extends FlatSpec with Matchers with ScalaFutures with DockerEtcdV3 with DockerTestKit with DockerKitDockerJava {
  //import EtcdV3Test._

  behavior of "EtcdV3Test"

  //implicit val pc = PatienceConfig(20 seconds, 1 second)

  it should "be able to launch etcd-v3 within Docker" in {
    dockerContainers.map(_.image).foreach(println)
    dockerContainers.forall(_.isReady().futureValue) shouldBe true

    whenReady(etcdV3Container.getPorts()) { ports =>
      info( s"etcdV3 using ports: $ports" )
    }
  }

  // tbd. tests using all/most aspects of 'jetcd'
}

/*** disabled
object EtcdV3Test {
  implicit def dur2span(dur: FiniteDuration): Span = {
    Span(dur.toMillis, Milliseconds)
  }
}
***/