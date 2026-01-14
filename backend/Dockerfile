# Build stage
FROM gradle:8.11.1-jdk17-alpine AS build
WORKDIR /home/gradle/project
COPY build.gradle settings.gradle ./
COPY src ./src
RUN gradle clean bootJar --no-daemon

# Run stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /home/gradle/project/build/libs/*.jar /app/app.jar
EXPOSE 7777
ENTRYPOINT ["java","-jar","/app/app.jar"]

