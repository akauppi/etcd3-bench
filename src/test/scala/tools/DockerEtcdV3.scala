package test.tools

import com.whisk.docker.{DockerContainer, DockerKit, DockerReadyChecker}

trait DockerEtcdV3 extends DockerKit {
  import DockerEtcdV3._

  val portBinding = ports.map( x => Tuple2(x,None) )

  val etcdV3Container = DockerContainer("quay.io/coreos/etcd:v3.0.16")
    .withPorts(portBinding:_*)
    .withReadyChecker(DockerReadyChecker.LogLineContains("etcdmain: ready to serve client requests"))

  abstract override def dockerContainers: List[DockerContainer] =
    etcdV3Container :: super.dockerContainers
}

object DockerEtcdV3 {
  // Note: port 2379 is client communication, 2380 server-to-server (we probably won't need that)
  private
  val ports: Seq[Int] = Seq(2379 /*, 2380*/)
}
