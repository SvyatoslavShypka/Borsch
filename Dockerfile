#Build stage
FROM gradle:8.0.2-bin AS BUILD
FROM gradle:8.0.2 AS BUILD
WORKDIR /usr/app/
COPY . .
RUN gradle clean build
COPY build ./build

# Package stage
FROM openjdk:latest
LABEL cicd="borsch3"

ARG JAR_FILE=Borsch-0.0.1-SNAPSHOT.jar

COPY /build/libs/${JAR_FILE} app.jar
EXPOSE 2665
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app.jar ${0} ${@}"]
