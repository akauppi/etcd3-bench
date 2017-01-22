# Todo

- making tests, exercising the API

- make a pre-test task that provides error if Docker is not running

---

- With Etcd3 version 3.1.0 we get:

```
[info] test.SomeTest *** ABORTED ***
[info]   java.lang.RuntimeException: Cannot run all required containers
[info]   at com.whisk.docker.DockerKit.startAllOrFail(DockerKit.scala:59)
[info]   at com.whisk.docker.DockerKit.startAllOrFail$(DockerKit.scala:43)
[info]   at test.SomeTest.startAllOrFail(SomeTest.scala:14)
[info]   at com.whisk.docker.scalatest.DockerTestKit.beforeAll(DockerTestKit.scala:21)
[info]   at com.whisk.docker.scalatest.DockerTestKit.beforeAll$(DockerTestKit.scala:19)
[info]   at test.SomeTest.beforeAll(SomeTest.scala:14)
[info]   at org.scalatest.BeforeAndAfterAll.liftedTree1$1(BeforeAndAfterAll.scala:212)
[info]   at org.scalatest.BeforeAndAfterAll.run(BeforeAndAfterAll.scala:210)
[info]   at org.scalatest.BeforeAndAfterAll.run$(BeforeAndAfterAll.scala:208)
[info]   at test.SomeTest.run(SomeTest.scala:14)
[info]   ...
```

3.0.16 seems to work.

