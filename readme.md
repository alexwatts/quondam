# Quondam

Quondam is a HA microservice that trys to help with making parts of a business flow happen 'only once' in infrastructural environments where 'at least once' is the default modus operandi.

It's built on top of Redis, Docker and Kubernetes. The REST protocol is serviced by a Spring Boot microservice using Spring MVC.

The performance and resiliency tests are based on Gatling. Quondam is fast and highly available.

## Getting Started

These instructions will explain how to build Quondam locally, deploy to a local kubernetes cluster (minikube), and run the performance tests locally against a quondiam cluster with 3 instances of redis in Master/Slave/Slave configuration and 3 Redis Sentinels to handle failover. 

### Prerequisites

Minikube.
Kubectla.
Maven.
Docker.
Java.


```
Give examples
```

### Installing

A step by step series of examples that tell you how to get a development env running

Say what the step will be

```
Give the example
```

And repeat

```
until finished
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

