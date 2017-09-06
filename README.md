## Welcome!

This repository is designed to allow Spring Data Neo4j and OGM users and team members to contribute self-contained projects
reproducing issues logged in the issue tracker at https://jira.spring.io and https://github.com/neo4j/neo4j-ogm/issues.  
It offers the following advantages :

* A streamlined process for evaluating issues so more bugs get fixed more quickly!
* A set of template projects to choose from
* Two-way communication with the SDN team based on working code

All around a better way of interacting with the Spring Data Neo4j team.

## Logging an issue against Spring's JIRA issue tracker

1. First, search [SDN JIRA](https://jira.spring.io/browse/DATAGRAPH) 
   or [OGM issue tracker](https://github.com/neo4j/neo4j-ogm/issues) to see if your issue has already
   been reported. If so, there may already be a reproduction issue in this repository!
1. If after searching an issue does not already exist, create a new issue
    * You will now have an issue named, for example, "DATAGRAPH-123".  Make note of this, as you'll need it
      below when creating your project.

## Using this repository to demonstrate the issue

Assuming you've encountered and created an issue in the issue tracker you can now add a
project to demonstrate it.

### First-time setup

1. [Create a Github account](https://github.com/signup/free) if you don't already have one
1. [Fork this repository and clone it locally](http://help.github.com/fork-a-repo/)

### Create a project that reproduces your issue

The idea is to create the smallest possible project to demonstrate the issue. The project may be built
with Maven and must contain only sources, XML and other necessary text files. No JARs, please!

For the purpose of these instructions, we'll assume your new JIRA issue ID is "DATAGRAPH-123"

#### Steps

1\. In your local clone of this repository, create a copy of the appropriate template directory.

For [SDN 5.x](https://github.com/neo4j-examples/neo4j-sdn-ogm-issue-report-template/tree/master/sdn-5.x) issues:

```bash
cd neo4j-sdn-ogm-issue-report-template
./create-repro-project.sh sdn-5.x DATAGRAPH-123
```

For [SDN 4.2](https://github.com/neo4j-examples/neo4j-sdn-ogm-issue-report-template/tree/master/sdn-4.2) issues:

```bash
cd neo4j-sdn-ogm-issue-report-template
./create-repro-project.sh sdn-4.2 DATAGRAPH-123
```

For [SDN 4.1](https://github.com/neo4j-examples/neo4j-sdn-ogm-issue-report-template/tree/master/sdn-4.1) issues:

```bash
cd neo4j-sdn-ogm-issue-report-template
./create-repro-project.sh sdn-4.1 DATAGRAPH-123
```

For [OGM 2.x](https://github.com/neo4j-examples/neo4j-sdn-ogm-issue-report-template/tree/master/ogm-2.x) issues:

```bash
cd neo4j-sdn-ogm-issue-report-template
./create-repro-project.sh ogm-2.x issue-123
```

For [SDN with Spring Boot 1.5](https://github.com/neo4j-examples/neo4j-sdn-ogm-issue-report-template/tree/master/boot-1.5) issues:

```bash
cd neo4j-sdn-ogm-issue-report-template
./create-repro-project.sh boot-1.5 DATAGRAPH-123
```

For [SDN with Spring Boot 1.4](https://github.com/neo4j-examples/neo4j-sdn-ogm-issue-report-template/tree/master/boot-1.4) issues:

```bash
cd neo4j-sdn-ogm-issue-report-template
./create-repro-project.sh boot-1.4 DATAGRAPH-123
```

2\. Review the list of dependencies and version numbers and modify the pom.xml as necessary.

3\. Import the project into your IDE and modify it as necessary to reproduce your issue.

* It is already a buildable Maven project, so you may use your IDE's built-in support for Maven to do
  the importing.

4\. Add, commit, and push your local fork

```bash
git add DATAGRAPH-123
git commit -m "Add repro project for DATAGRAPH-123"
git push
```

5\. [Send a pull request from the Github web interface](https://help.github.com/articles/creating-a-pull-request)

* The SDN team will be notified and will look at your request

... and that's it!

## FAQ

### What if my issue is not a bug, but an improvement or new feature request?

In certain cases, it may make sense to submit a project for improvement requests.  Feel free to submit a project
here for your issue if you think it will help us to understand the scenario better.

### What about patches against Spring Data Neo4j itself? Should I still attach those to my JIRA issue?

Yes, patches against JIRA will always be supported, but keep in mind that the Spring Data Neo4j project itself
is hosted on GitHub as well, and pull requests are even better! See the "Contributing" section of the Spring
Data Neo4j [readme](https://github.com/spring-projects/spring-data-neo4j#contributing-to-spring-data-neo4j) for details.

### Can I still attach .zip files to my JIRA issue?  Is it required to use this repository?

Yes, you may still attach zip files if it works best for you.  Submitting pull requests against this repository
as described above is the mechanism that the SDN team prefers, but what's most important is that we get code
from you that reproduces the problem!  Please consider this approach, but zip files are still OK.
