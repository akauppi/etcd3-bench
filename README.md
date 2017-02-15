# etcd3-bench

Trying out [etcd v3](https://coreos.com/etcd/docs/latest/) API from Scala

## Requirements

- `docker-machine` or Docker native
- `sbt`

The development environment is macOS with HomeBrew but that shouldn't matter.

---

<!-- tbd. Remove this once `coreos/jetcd` is properly published.

Follow this [issue](https://github.com/coreos/jetcd/issues/66) to see the publishing situation.
-->

Until [coreos/jetcd](https://github.com/coreos/jetcd) is properly published, we need to build and install a local copy of it:

```
$ git clone https://github.com/coreos/jetcd.git
$ cd jetcd
$ mvn install -DskipTests
```

That installs `coreos/jetcd` in the local Maven.

---

## Getting started

Launch Docker (for `docker-machine`):

```
$ docker-machine start default
$ eval $(docker-machine env)
```

<!-- tbd. how is it?
Launch Docker (for Docker native):

```
$ docker info     # tbd. is this correct?
```
-->

```
$ sbt test
```

The tests should pass.

This launches a local etcd3 instance, for the duration of the tests, and runs the stated tests against it.


## Troubleshooting

If you see the error:

```
[info] test.EtcdV3Test *** ABORTED ***
java.lang.RuntimeException: Cannot run all required containers
```

..the reason is likely that your `docker-machine` is out-of-date. Run this:

```
$ docker-machine upgrade default
$ eval $(docker-machine env)
```

Try `sbt test` again.

If you still get the same error, try to launch etcd manually (see [docs](docs/) for instructions).

```
$ docker run quay.io/coreos/etcd:v3.1.0
```
