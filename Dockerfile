
#Build stage
#FROM gradle:8.0.2-bin AS BUILD
FROM gradle:8.0.2 AS BUILD
WORKDIR /usr/app/
COPY . .
RUN gradle clean build

# Package stage
FROM openjdk:latest
#RUN ./gradlew clean build
LABEL cicd="borsch3"
VOLUME /tmp
ARG JAR_FILE=/build/libs/Borsch-0.0.1-SNAPSHOT.jar
COPY build/libs/${JAR_FILE} app.jar
EXPOSE 7778
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app.jar ${0} ${@}"]