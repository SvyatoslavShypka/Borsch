# Part 1: Build
FROM gradle:8.0.2 AS build
WORKDIR /usr/app
COPY . .
RUN gradle clean build --no-daemon

# Part 2: Package stage
#FROM openjdk:17-jdk-slim
FROM openjdk:latest
#Możesz użyć innej wersji JDK, np. OpenJDK 17
LABEL cicd="borsch3"
WORKDIR /app

ARG JAR_FILE=Borsch-0.0.1-SNAPSHOT.jar
# Skopiuj plik JAR z fazy build do finalnego obrazu
COPY --from=build /usr/app/build/libs/${JAR_FILE} app.jar
#port for postgres
EXPOSE 2665

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app/app.jar ${0} ${@}"]
