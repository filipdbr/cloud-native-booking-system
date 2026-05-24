### Multi-stage build ###

## Stage 1: build the app

# Base build environment: Maven with Eclipse Temurin Java 21 (LTS) JDK
FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder

WORKDIR /app

# install dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# copy source code
COPY src ./src

# build the app
RUN mvn package -DskipTests -B

## Stage 2: runtime

# Base runtime: Eclipse Temurin Java 21 (LTS) JRE
# Alpine Linux distribution used for minimal footprint and enhanced security
FROM eclipse-temurin:21-jre-alpine AS runtime

WORKDIR /app

# create a user and group to prevent using root user in the image
# -S flag in adduser used for creation of a system account
RUN addgroup -S app && adduser -G app -S appuser

# copy jar from building stage
COPY --from=builder /app/target/*.jar app.jar

USER appuser

EXPOSE 8080

# CMD execution wrapper (exec form) ensures SIGTERM goes directly to JVM and not to shell
CMD ["java", "-jar", "app.jar"]