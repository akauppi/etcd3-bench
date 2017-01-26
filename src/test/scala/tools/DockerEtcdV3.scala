package test.tools

import com.whisk.docker.{DockerContainer, DockerKit, DockerReadyChecker, LogLineReceiver}

trait DockerEtcdV3 extends DockerKit {
  import DockerEtcdV3._

  private
  val portBinding = ports.map( x => Tuple2(x,None) )

  // Note: etcd 3.1.0 requires the below arguments.
  //    - '0.0.0.0' for exposing the port to Docker host side (default 'localhost' is not enough).
  //    - '-listen...' needed as pairs for the '-*advertise*'.
  //    - peer URLs needed, otherwise: "listen tcp 10.169.44.38:2380: bind: cannot assign requested address"
  //
  // tbd. Once we have actual tests, see which params really are needed.
  //
  private
  val cmd: Seq[String] = Seq(
    "/usr/local/bin/etcd",
    "--advertise-client-urls", "http://0.0.0.0:2379",
    "--listen-client-urls", "http://0.0.0.0:2379",
    "--initial-advertise-peer-urls", "http://0.0.0.0:2380",
    "--listen-peer-urls", "http://0.0.0.0:2380"
  )

  val etcdV3Container = DockerContainer(s"quay.io/coreos/etcd:v$version")
    .withCommand(cmd:_*)
    .withPorts(portBinding:_*)
    .withReadyChecker(DockerReadyChecker.LogLineContains(s"$prefix: ready to serve client requests"))

  abstract override def dockerContainers: List[DockerContainer] =
    etcdV3Container :: super.dockerContainers
}

object DockerEtcdV3 {
  private
  val (version, prefix) = ("3.1.0", "embed")    // or ("3.0.16", "etcdmain")

  // Note: port 2379 is client communication, 2380 server-to-server (we probably won't need that)
  private
  val ports: Seq[Int] = Seq(2379 /*, 2380*/)
}
