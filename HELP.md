# Getting Started

### Register a new user or use "test@test.com" with a password "Test_001"

For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.2.2/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.2.2/gradle-plugin/reference/html/#build-image)
* [Flyway Migration](https://docs.spring.io/spring-boot/docs/3.2.2/reference/htmlsingle/index.html#howto.data-initialization.migration-tool.flyway)
* [Thymeleaf](https://docs.spring.io/spring-boot/docs/3.2.2/reference/htmlsingle/index.html#web.servlet.spring-mvc.template-engines)
* [Spring Data JDBC](https://docs.spring.io/spring-boot/docs/3.2.2/reference/htmlsingle/index.html#data.sql.jdbc)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.2.2/reference/htmlsingle/index.html#data.sql.jpa-and-spring-data)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/3.2.2/reference/htmlsingle/index.html#using.devtools)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.2.2/reference/htmlsingle/index.html#web)
* [Docker Compose Support](https://docs.spring.io/spring-boot/docs/3.2.2/reference/htmlsingle/index.html#features.docker-compose)
* [Spring Security](https://docs.spring.io/spring-boot/docs/3.2.2/reference/htmlsingle/index.html#web.security)
* [Rest Repositories](https://docs.spring.io/spring-boot/docs/3.2.2/reference/htmlsingle/index.html#howto.data-access.exposing-spring-data-repositories-as-rest)

### Guides

The following guides illustrate how to use some features concretely:

* [Handling Form Submission](https://spring.io/guides/gs/handling-form-submission/)
* [Using Spring Data JDBC](https://github.com/spring-projects/spring-data-examples/tree/master/jdbc/basics)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
* [Spring Boot and OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
* [Authenticating a User with LDAP](https://spring.io/guides/gs/authenticating-ldap/)
* [Accessing JPA Data with REST](https://spring.io/guides/gs/accessing-data-rest/)
* [Accessing Neo4j Data with REST](https://spring.io/guides/gs/accessing-neo4j-data-rest/)
* [Accessing MongoDB Data with REST](https://spring.io/guides/gs/accessing-mongodb-data-rest/)

### Additional Links

These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

### Docker Compose support
This project contains a Docker Compose file named `compose.yaml`.
In this file, the following services have been defined:

* postgres: [`postgres:latest`](https://hub.docker.com/_/postgres)

Please review the tags of the used images and set them to the same as you're running in production.

### CI/CD
Clone the project locally
Use bash ruh.sh to start CI/CD
Use bash ruh.sh [--force | -f] to force the script to pull the latest code
Use bash ruh.sh [--cleanup | -cu] to force the script to pull the latest code
Use bash ruh.sh [--full_cleanup | -fcu] the script prunes only images with the CI/CD label
