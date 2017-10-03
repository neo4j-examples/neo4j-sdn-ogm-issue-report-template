# Repro project for boot-2.0

## Configuring

First, check out and update the versions in the `pom.xml` to match yours

- the `spring-data-releasetrain.version`
- the `neo4j-ogm.version`

Note that the `spring-data-releasetrain.version` is the prefered way to pull coherent dependency versions of spring data projects.
For more detailed information about release trains, see [Spring Data project homepage](http://projects.spring.io/spring-data/).

You can also override the versions of SDN and the Neo4j java drivers.

## Logging

Logging is configured in `application.properties`. See Spring boot documentation for more details.
