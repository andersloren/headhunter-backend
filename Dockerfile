# First stage: prepare maven
FROM eclipse-temurin:21-jdk-jammy AS base
WORKDIR /build
COPY --chmod=0755 mvnw mvnw
COPY .mvn/ .mvn/

# Second stage: cache dependencies in image
FROM base AS deps
WORKDIR /build
RUN --mount=type=bind,source=pom.xml,target=pom.xml \
--mount=type=cache,target=/root/.m2 \
./mvnw dependency:go-offline -DskipTests

# Third stage: run tests
FROM deps AS build
WORKDIR /build
COPY ./src src/
COPY src/test/resources/application-integration-test.yml /app/
COPY src/test/resources/application-test.yml /app/
COPY src/test/resources/application-mock-test.yml /app/
RUN --mount=type=secret,id=DB_NAME \
--mount=type=secret,id=DB_TEST_NAME \
--mount=type=secret,id=DB_TIMEZONE \
--mount=type=secret,id=DB_PORT \
--mount=type=secret,id=DB_ROOT_USERNAME \
--mount=type=secret,id=DB_INTEGRATION_TESTS_NAME \
--mount=type=secret,id=DB_ROOT_PASSWORD \
--mount=type=secret,id=DB_PROD_DATABASE_NAME \
--mount=type=secret,id=DB_PROD_HOSTNAME \
--mount=type=secret,id=DB_USER_USERNAME \
--mount=type=secret,id=DB_USER_PASSWORD \
--mount=type=secret,id=DB_DEV_DATABASE_NAME \
--mount=type=secret,id=DB_DEV_HOSTNAME \
--mount=type=secret,id=DB_INTEGRATION_TESTS_DATABASE_NAME \
--mount=type=secret,id=DB_INTEGRATION_TESTS_HOSTNAME \
--mount=type=secret,id=DB_ADMIN_USERNAME \
--mount=type=secret,id=DB_ADMIN_PASSWORD \
--mount=type=secret,id=DB_TEST_DATABASE_NAME \
--mount=type=secret,id=DB_TEST_USERNAME \
--mount=type=secret,id=DB_TEST_PASSWORD \
--mount=type=secret,id=PRODUCTION_PROFILE \
--mount=type=secret,id=DEVELOP_PROFILE \
--mount=type=secret,id=DB_INTEGRATION_TESTS_PROFILE \
--mount=type=secret,id=DB_TESTS_PROFILE \
--mount=type=secret,id=DB_MOCK_TESTS_PROFILE \
--mount=type=secret,id=CORS_ALLOWED_ORIGIN \
--mount=type=secret,id=REACT_PROFILE \
--mount=type=secret,id=OPENAI_API_ENDPOINT \
--mount=type=secret,id=OPENAI_API_KEY \
--mount=type=secret,id=OPENAI_API_MODEL \
--mount=type=bind,source=pom.xml,target=pom.xml \
--mount=type=cache,target=/root/.m2 \
sh -c 'for secret in /run/secrets/*; do varname=$(basename "$secret"); export "$varname"="$(cat "$secret")"; done; \
./mvnw test \
-e \
-X \
-Dspring.config.location=/app/application-integration-test.yml,/app/application-test.yml,/app/application-mock-test.yml \
-Dspring.profiles.active="${DB_MOCK_TESTS_PROFILE},${DB_TESTS_PROFILE},${DB_INTEGRATION_TESTS_PROFILE}"'

# Fourth stage: create package and rename it
FROM build AS package
WORKDIR /build
RUN --mount=type=bind,source=pom.xml,target=pom.xml \
--mount=type=cache,target=/root/.m2 \
./mvnw package -DskipTests && mv target/$(./mvnw help:evaluate -Dexpression=project.artifactId -q -DforceStdout)-$(./mvnw help:evaluate -Dexpression=project.version -q -DforceStdout).jar target/app.jar

# Fifth stage: extract layers
FROM package AS extract
WORKDIR /build
RUN java -Djarmode=layertools -jar target/app.jar extract --destination target/extracted

# Sixth stage: copy layers, prepare debugger, and set the active profile
FROM extract AS development
WORKDIR /build
RUN cp -r /build/target/extracted/dependencies/. ./
RUN cp -r /build/target/extracted/spring-boot-loader/. ./
RUN cp -r /build/target/extracted/snapshot-dependencies/. ./
RUN cp -r /build/target/extracted/application/. ./
ENV JAVA_TOOL_OPTIONS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8080"
CMD [ "java", "-Dspring.profiles.active=${PRODUCTION_PROFILE}", "org.springframework.boot.loader.launch.JarLauncher"]

# Seventh stage: create user, finalize build
FROM eclipse-temurin:21-jre-jammy AS final
ARG UID=10001
RUN adduser \
--disabled-password \
--home "/nonexistent" \
--shell "/sbin/nologin" \
--no-create-home \
--uid "${UID}" \
appuser
USER appuser
COPY --from=extract /build/target/extracted/dependencies/. ./
COPY --from=extract /build/target/extracted/spring-boot-loader/. ./
COPY --from=extract /build/target/extracted/snapshot-dependencies/. ./
COPY --from=extract /build/target/extracted/application/. ./
COPY src/main/resources/application-prod.yml /app/
ENTRYPOINT [ "java","-Dspring.profiles.active=prod", "org.springframework.boot.loader.launch.JarLauncher"]
