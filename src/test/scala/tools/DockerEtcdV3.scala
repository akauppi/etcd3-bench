package test.tools

import com.whisk.docker.{DockerContainer, DockerKit, DockerReadyChecker}

trait DockerEtcdV3 extends DockerKit {
  import DockerEtcdV3._

  val etcdV3Container = DockerContainer("quay.io/coreos/etcd:v3.0.16")
    .withPorts(port -> None)
    .withReadyChecker(DockerReadyChecker.LogLineContains("etcdmain: ready to serve client requests"))

  abstract override def dockerContainers: List[DockerContainer] =
    etcdV3Container :: super.dockerContainers
}

object DockerEtcdV3 {
  private
  val port: Int = 2739
}
