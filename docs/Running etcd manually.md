# Running etcd manually

We wish to run just a single instance of `etcd` so that we're able to communicate with it. No clustering.


## Running locally on macOS with HomeBrew

Truly a piece of 🍰.

```
$ brew install etcd
...
$ brew services start etcd    # starts in `localhost:2379`
$ ETCDCTL_API=3 etcdctl put mykey "this is awesome"
$ ETCDCTL_API=3 etcdctl get mykey
mykey
this is awesome
```

That's it. 


## Running on Docker

The instructions are for macOS with `docker-machine`.

```
$ docker run -p 2379:2379 --name etcd quay.io/coreos/etcd:v3.1.0 /usr/local/bin/etcd -advertise-client-urls http://0.0.0.0:2379 -listen-client-urls http://0.0.0.0:2379
...
2017-01-19 21:46:10.181142 I | etcdmain: ready to serve client requests
2017-01-19 21:46:10.181441 N | etcdmain: serving insecure client requests on localhost:2379, this is strongly discouraged!
```

- The `-listen-client-urls` parameter is needed for the host to reach the docker port. Details [here](http://stackoverflow.com/questions/35577856/running-etcd-in-docker-container).

The `-p` parameter is important: it exposes the Docker etcd client port to the host (the other port, 2380, is for server-to-server comms and we don't need it).

Hint: Add `--rm` if you want the container to be automatically removed after being stopped.

### Accessing from host

In another window, or add `-d` to the above command:

```
$ ETCDCTL_API=3 etcdctl --endpoints http://$(docker-machine ip):2379 put mykey "this is awesome"
```

### Accessing within Docker

We can also use the `etcdctl` within the Docker container. It's as fast as the host side.

```
$ docker exec etcd /bin/sh -c "export ETCDCTL_API=3 && /usr/local/bin/etcdctl put mykey awesome"
```

### Misc about `etcdctl` 

Note that `etcdctl` provides a different set of commands (and different `--help` output) when `ETCDCTL_API=3` is declared.

Other commands:

- `get foo`
- `help`
- `endpoint health`

## References

- coreos/[etcd](https://github.com/coreos/etcd) (GitHub)
- [Running etcd in Docker Container](http://stackoverflow.com/questions/35577856/running-etcd-in-docker-container) (StackOverflow)
