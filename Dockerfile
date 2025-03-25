FROM openjdk:17-jdk-alpine

# Set the working directory in the container
WORKDIR /app

COPY target/user-management-service-0.0.1-SNAPSHOT.jar user-management-service.jar

# EXPOSE 8081:8081

# run the jar file
ENTRYPOINT ["java", "-jar", "user-management-service.jar"]