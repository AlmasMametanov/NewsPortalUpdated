# NewsPortalUpdated
It's REST API project using Spring Boot and Spring Security.<br/>
The project includes the implementation of authentication and authorization <br/>
using JWT tokens to securely transfer data between client and server. <br/>
Redis caching technology is used. <br/>
The project also implemented unit tests.<br/>
Docker is used to simplify deployment and project management.<br/>
## Technologies
* Spring Boot, Spring Security, Spring Data JPA
* Redis, JWT, Junit 5, Mockito, AssertJ, Log4j2
* Java 11, Postgresql, Tomcat, Maven, Docker
## Deployment instructions with Docker
1. You have to have Docker on your computer.
2. Copy the docker-compose.yaml file to yourself
3. Create yourself a directory ./src/main/resources/db/migration, <br/>
where copy the file docker-entrypoint-initdb.sql
4. Open a terminal, go to the directory with docker-compose.yaml, <br/>
and run the command 'docker compose up -d'.

