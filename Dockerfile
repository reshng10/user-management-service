# Build stage
FROM maven:3.8-openjdk-17 AS maven_build
COPY pom.xml /build/
COPY src /build/src
WORKDIR /build/
RUN mvn clean package


# Run stage
FROM openjdk:17-jdk-alpine

# Set the working directory in the container
WORKDIR /app

COPY --from=maven_build  /build/target/user-management-service-0.0.1-SNAPSHOT.jar user-management-service.jar

# EXPOSE 8081:8081

# run the jar file
ENTRYPOINT ["java", "-jar", "user-management-service.jar"]