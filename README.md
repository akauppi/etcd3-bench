# etcd3-bench

Trying out [etcd v3](https://coreos.com/etcd/docs/latest/) API from Scala

## Development environment

- macOS with HomeBrew
- `docker-machine`

Should also work with Docker native, but not tested.


## Getting started

```
$ docker-machine start default
$ eval $(docker-machine env)
```

```
$ sbt test
```

