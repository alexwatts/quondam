# Quondam

Quondam is a HA microservice that provides an interface for making things happen 'only once', for environments where 'at least once' is an implication of the technical infrastructure, for instance where queues are involved.

It's built on top of Redis, Docker and Kubernetes. The REST protocol is serviced by a Spring Boot microservice. 

The performance and resiliency tests are based on Gatling. 

Quondam is fast and highly available.

## Getting Started

These instructions will explain how to build Quondam locally, deploy to a local kubernetes cluster (minikube), and run the performance tests locally against a quondam cluster with 3 instances of redis in master/slave/slave configuration and 3 Redis Sentinels to handle failover and master re-election. 

### Prerequisites

Minikube.
Kubectl.
Maven.
Docker.
Java.

You can follow the links in each pre-requisite to see how to install these on MAC OS 10 Mojave

### Installing

To build quondam, quondam-nft and payments you need minikube to be running. (what am i building?)

```
 eval $(minikube docker-env)
 mvn clean install
```

You should see the following output:

```
[INFO] ------------------------------------------------------------------------
[INFO] Reactor Summary:
[INFO] 
[INFO] quondam ............................................ SUCCESS [ 45.352 s]
[INFO] quondam-nft......................................... SUCCESS [  1.568 s]
[INFO] payments ........................................... SUCCESS [ 17.262 s]
[INFO] bond-parent ........................................ SUCCESS [  0.009 s]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

To build the redis image 

```
./build-redis-image.sh
```

You should see lines similar to this:

```
Successfully built e33e8d456e34
Successfully tagged k8s.gcr.io/redis:v1
```

To create the redis cluster:

```
./create-redis-cluster.sh
```

you should see these lines:

```
pod/redis-master created
service/redis-sentinel created
replicationcontroller/redis-sentinel created
replicationcontroller/redis created
replicationcontroller/redis scaled
replicationcontroller/redis-sentinel scaled
```

You should then be able to observe the redis cluster is up and running, with

```
kubectl get pods
```

and see these running pods (3 redis, 3 sentinels)

```
NAME                            READY   STATUS    RESTARTS   AGE
redis-msxwc                     1/1     Running   0          2m49s
redis-nzfxs                     1/1     Running   0          2m49s
redis-sentinel-rj2n9            1/1     Running   0          2m49s
redis-sentinel-wkvvq            1/1     Running   0          2m49s
redis-sentinel-kv6io            1/1     Running   0          2m49s
redis-v9r5r                     1/1     Running   0          2m50s
```

To create the quondom REST API,

```
./create-quondom.sh
```

You should see quondam added to the list of running pods

```
NAME                            READY   STATUS    RESTARTS   AGE
quondam-web-64fcf46bd9-skfvv    1/1     Running   0          2m49s
redis-msxwc                     1/1     Running   0          2m49s
redis-nzfxs                     1/1     Running   0          2m49s
redis-sentinel-rj2n9            1/1     Running   0          2m49s
redis-sentinel-wkvvq            1/1     Running   0          2m49s
redis-sentinel-kv6io            1/1     Running   0          2m49s
redis-v9r5r                     1/1     Running   0          2m50s
```

To create the payments API, 

```
./create-payments.sh
```

You should see the payments added to the list of running pods

```
NAME                            READY   STATUS    RESTARTS   AGE
quondam-web-64fcf46bd9-skfvv    1/1     Running   0          2m49s
payments-web-8467f5b867-4jz74   1/1     Running   0          2m49s
redis-msxwc                     1/1     Running   0          2m49s
redis-nzfxs                     1/1     Running   0          2m49s
redis-sentinel-rj2n9            1/1     Running   0          2m49s
redis-sentinel-wkvvq            1/1     Running   0          2m49s
redis-sentinel-kv6io            1/1     Running   0          2m49s
redis-v9r5r                     1/1     Running   0          2m50s
```

## Running the nft tests

The NFT tests are based on gatling to load the quondam web service. There is a shell script (chaos.sh) that is invoked periodically by the load test to randomly destoy redis nodes while the test is running to prove resiliency.

Check the service URL for quondam 

```
minikube service --url quondam-web
```

you should see output similar to 

```
http://192.168.99.101:32342
```

Check the service URL for payments

```
minikube service --url payments-web
```

you should see output similar to 

```
http://192.168.99.101:30478
```

Update the performance test config to match the quondam and payments URLs

```
vi quondam-nft/src/test/resources/application.conf
```

make sure these lines reflect the actual service URLs

```
urls {
  bondUrl = "http://192.168.99.101:32342"
  paymentsUrl = "http://192.168.99.101:30478"
}  
```

run the quondam NFT's

```
./run-nft.sh
```

The NFTs will do some setup, 'creating keys' etc, but after they get going you will see some repeating output like this. The global request count should always be increasing...

```
================================================================================
2019-06-05 22:19:37                                         165s elapsed
---- Requests ------------------------------------------------------------------
> Global                                                   (OK=16426  KO=0     )
> delete_key                                               (OK=10118  KO=0     )
> Make Payment                                             (OK=6308   KO=0     )

---- Chaos ---------------------------------------------------------------------
[--------------------------------------------------------------------------]  0%
          waiting: 0      / active: 1      / done: 0     
---- Claim Keys ----------------------------------------------------------------
[--------------------------------------------------------------------------]  0%
          waiting: 0      / active: 5      / done: 0     
================================================================================
```

A simply way to observe the pods while the test is running is to open a 'watch' command on 'kubectl get pods'. You can get watch on mac OS with

```
brew install watch
```

in a second terminal window, you can run 

```
eval $(minikube docker-env)
watch kubectl get pods
```
to get the status of all running pods

```
Every 2.0s: kubectl get pods                         I: Wed Jun  5 21:51:49 2019

NAME                            READY   STATUS    RESTARTS   AGE
bond-web-64fcf46bd9-skfvv       1/1     Running   0          161m
payments-web-8467f5b867-4jz74   1/1     Running   0          159m
redis-master                    2/2     Running   0          25m
redis-msxwc                     1/1     Running   0          25m
redis-nzfxs                     1/1     Running   0          25m
redis-sentinel-rj2n9            1/1     Running   0          25m
redis-sentinel-wkvvq            1/1     Running   0          25m
redis-v9r5r                     1/1     Running   0          25m
```


Periodically you should see the redis pods cycling when the chaos script kicks in eg..:

```
Every 2.0s: kubectl get pods                         I: Wed Jun  5 22:30:13 2019

NAME                            READY   STATUS              RESTARTS   AGE
bond-web-64fcf46bd9-skfvv       1/1     Running             0          3h19m
payments-web-8467f5b867-mc5p7   1/1     Running             0          29m
redis-kc76b                     0/1     ContainerCreating   0          4s
redis-master                    2/2     Running             0          64m
redis-msxwc                     1/1     Running             0          63m
redis-nzfxs                     1/1     Running             0          63m
redis-sentinel-rj2n9            1/1     Running             0          63m
redis-sentinel-wkvvq            1/1     Running             0          63m
redis-v9r5r                     1/1     Terminating         0          63m
```
When the tests are finished (10 minutes) You should see some output similar to the below

```
================================================================================
---- Global Information --------------------------------------------------------
> request count                                      61276 (OK=61260  KO=16    )
> min response time                                      0 (OK=0      KO=7502  )
> max response time                                   7573 (OK=6417   KO=7573  )
> mean response time                                    49 (OK=47     KO=7520  )
> std deviation                                        215 (OK=178    KO=20    )
> response time 50th percentile                         13 (OK=13     KO=7514  )
> response time 75th percentile                         52 (OK=52     KO=7523  )
> response time 95th percentile                        100 (OK=100    KO=7565  )
> response time 99th percentile                       1521 (OK=718    KO=7571  )
> mean requests/sec                                 99.152 (OK=99.126 KO=0.026 )
---- Response Time Distribution ------------------------------------------------
> t < 800 ms                                         60649 ( 99%)
> 800 ms < t < 1200 ms                                   0 (  0%)
> t > 1200 ms                                          611 (  1%)
> failed                                                16 (  0%)
---- Errors --------------------------------------------------------------------
> status.find.is(200), but actually found 500                        16 (100.0%)
================================================================================

Reports generated in 3s.
Please open the following file: /Users/alex/bond/bond-perf-tests/target/gatling/hasimulation-20190606081951143/index.html
Global: mean of response time is less than 50.0 : true
delete_key: percentage of failed events is less than 0.05 : true
Make Payment: percentage of failed events is less than 0.05 : true
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

The key assertions in the test are that the error request percentage is lower that 0.05% and that the mean response time is less than 50ms.


## Authors

* **Alex Watts** - (https://github.com/alexwatts)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* https://github.com/kubernetes
* https://spring.io/projects/spring-boot
* https://github.com/redis
* https://github.com/redisson

