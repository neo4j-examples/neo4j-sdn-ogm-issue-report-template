# Repro project for ticket-4217

## Configuring

First, check out and update the versions in the `pom.xml` to match yours

- the `spring.version`
- the `spring-data-releasetrain.version`
- the `neo4j-ogm.version`

Note that the `spring-data-releasetrain.version` is the prefered way to pull coherent dependency versions of spring data projects.
For more detailed information about release trains, see [Spring Data project homepage](http://projects.spring.io/spring-data/).

You can also override the versions of SDN and the Neo4j java drivers.

## Logging

This project contains a `logback-test.xml` file in `src/test/resources` that you
may wish to configure to emit more detailed logging.