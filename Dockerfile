FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ./target/headhunter-backend-0.0.1-SNAPSHOT.jar headhunter-backend.jar
ENTRYPOINT ["java", "-jar", "/headhunter-backend.jar"]