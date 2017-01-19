package test

import com.whisk.docker.DockerFactory
import com.whisk.docker.impl.dockerjava.DockerKitDockerJava
import com.whisk.docker.scalatest.DockerTestKit
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Milliseconds, Span}
import org.scalatest.{FlatSpec, Matchers}
import test.tools.DockerEtcdV3

import scala.concurrent.duration._
import scala.languageFeature.postfixOps

class SomeTest extends FlatSpec with Matchers with ScalaFutures with DockerEtcdV3 with DockerTestKit with DockerKitDockerJava {
  //import SomeTest._

  behavior of "SomeTest"

  it should "pass" in {
    succeed
  }

  //implicit val pc = PatienceConfig(20 seconds, 1 second)

  "all containers" should "be ready at the same time" in {
    dockerContainers.map(_.image).foreach(println)
    dockerContainers.forall(_.isReady().futureValue) shouldBe true

    whenReady(etcdV3Container.getPorts()) { ports =>
      info( s"etcdV3 using ports: $ports" )
    }
  }
}

/*** disabled
object SomeTest {
  implicit def dur2span(dur: FiniteDuration): Span = {
    Span(dur.toMillis, Milliseconds)
  }
}
***/