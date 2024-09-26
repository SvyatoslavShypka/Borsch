
#Build stage
FROM gradle:latest AS BUILD
WORKDIR /usr/app/
COPY . .
RUN gradle build

# Package stage
FROM openjdk:latest
#RUN ./gradlew clean build
LABEL cicd="borsch3"
VOLUME /tmp
ARG JAR_FILE=/build/libs/Borsch-0.0.1-SNAPSHOT.jar
COPY build/libs/${JAR_FILE} app.jar
EXPOSE 7778
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app.jar ${0} ${@}"]