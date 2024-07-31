FROM openjdk:17-jdk-alpine

# Define the JAR file location
ARG JAR_FILE=target/headhunter-backend-0.0.1-SNAPSHOT.jar

# Copy the JAR file to the container
COPY ${JAR_FILE} app.jar

# Define the entry point to run the JAR file
ENTRYPOINT ["java", "-jar", "/app.jar"]