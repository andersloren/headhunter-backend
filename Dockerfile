# Minimal docker file (https://www.baeldung.com/spring-boot-docker-start-with-profile)

FROM openjdk:17-jdk-alpine
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "/app.jar"]