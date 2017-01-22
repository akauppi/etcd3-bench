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

```
$ sbt test
```
