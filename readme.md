# Quondam

Quondam is a HA microservice that trys to help with making parts of a business flow happen 'only once' in environments where 'at least once' is an implication of the technical infrastructure (when event sourcing with queues).

It's built on top of Redis, Docker and Kubernetes. The REST protocol is serviced by a Spring Boot microservice using Spring MVC.

The performance and resiliency tests are based on Gatling. Quondam is fast and highly available.

## Getting Started

These instructions will explain how to build Quondam locally, deploy to a local kubernetes cluster (minikube), and run the performance tests locally against a quondiam cluster with 3 instances of redis in Master/Slave/Slave configuration and 3 Redis Sentinels to handle failover. 

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
```

```
mvn clean install
```

You should see the following output:

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


To build the redis image 

```
./build-redis-image.sh
```

End with an example of getting some data out of the system or using it for a little demo

## Running the tests

Explain how to run the automated tests for this system

### Break down into end to end tests

Explain what these tests test and why

```
Give an example
```

### And coding style tests

Explain what these tests test and why

```
Give an example
```

## Deployment

Add additional notes about how to deploy this on a live system

## Built With

* [Dropwizard](http://www.dropwizard.io/1.0.2/docs/) - The web framework used
* [Maven](https://maven.apache.org/) - Dependency Management
* [ROME](https://rometools.github.io/rome/) - Used to generate RSS Feeds

## Contributing

Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/your/project/tags). 

## Authors

* **Billie Thompson** - *Initial work* - [PurpleBooth](https://github.com/PurpleBooth)

See also the list of [contributors](https://github.com/your/project/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* Hat tip to anyone whose code was used
* Inspiration
* etc

