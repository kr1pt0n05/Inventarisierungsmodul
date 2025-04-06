# syntax=docker/dockerfile:1
# escape=`


# Stage 1: Build
FROM maven:3.8.6-openjdk-17 as build
WORKDIR /app
COPY pom.xml ./
COPY src ./src
RUN mvn clean package -DskipTests


# Stage 2: Production
FROM openjdk:17-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
