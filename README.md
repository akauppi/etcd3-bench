# etcd3-bench

Trying out [etcd v3](https://coreos.com/etcd/docs/latest/) API from Scala

## Requirements

- `docker-machine` or Docker native
- `sbt`

The development environment is macOS with HomeBrew but that shouldn't matter.

Launch Docker (for `docker-machine`):

```
$ docker-machine upgrade
```

```
$ docker-machine start default
$ eval $(docker-machine env)
```

Launch Docker (for Docker native):

```
$ docker info     # tbd. is this correct?
```

## Getting started

<!-- tbd. Remove this once `coreos/jetcd` is properly published.

Follow this [issue](https://github.com/coreos/jetcd/issues/66) to see the publishing situation.
-->

Until [coreos/jetcd](https://github.com/coreos/jetcd) is properly published, we need to build and install a local copy of it:

```
$ git clone https://github.com/coreos/jetcd.git
$ cd jetcd
$ mvn install -DskipTests
```

That installs `coreos/jetcd` in a local Maven 

```
$ sbt test
```
