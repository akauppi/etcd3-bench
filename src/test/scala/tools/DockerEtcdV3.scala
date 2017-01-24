package test.tools

import com.whisk.docker.{DockerContainer, DockerKit, DockerReadyChecker, LogLineReceiver}

trait DockerEtcdV3 extends DockerKit {
  import DockerEtcdV3._

  private
  val portBinding = ports.map( x => Tuple2(x,None) )

  // Note: etcd 3.1.0 requires the below arguments. '0.0.0.0' for exposing the port to Docker host side (default
  //    'localhost' is not enough). '--listen...' needed as pairs for the '*advertise*'. And so on.
  //
  private
  val cmd: Seq[String] = Seq(
    "/usr/local/bin/etcd",
    "--advertise-client-urls", "http://0.0.0.0:2379", "--listen-client-urls", "http://0.0.0.0:2379",
    "--initial-advertise-peer-urls", "http://0.0.0.0:2380", "--listen-peer-urls", "http://0.0.0.0:2380"
  )

  // Note: 3.0.16 clear text is prefixed: "etcdmain: ready to serve client requests"
  //      3.1.0 has it "embed: ..."

  val etcdV3Container = DockerContainer("quay.io/coreos/etcd:v3.1.0")
    .withCommand(cmd:_*)
    .withPorts(portBinding:_*)
    .withReadyChecker(DockerReadyChecker.LogLineContains("embed: ready to serve client requests"))

  abstract override def dockerContainers: List[DockerContainer] =
    etcdV3Container :: super.dockerContainers
}

object DockerEtcdV3 {
  // Note: port 2379 is client communication, 2380 server-to-server (we probably won't need that)
  private
  val ports: Seq[Int] = Seq(2379 /*, 2380*/)
}
