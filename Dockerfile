FROM eclipse-temurin:21

WORKDIR /app

COPY target/*.jar /app/japp.jar
COPY target/classes/application-prod.yml /app/

ENTRYPOINT ["java","-Dspring.config.location=/app/application-prod.yml", "-jar", "/app/japp.jar"]

