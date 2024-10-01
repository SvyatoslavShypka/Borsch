#Build stage
#FROM gradle:8.0.2-bin AS BUILD
#FROM gradle:8.0.2 AS BUILD
#WORKDIR /usr/app/
#COPY . .
#RUN gradle clean build
#COPY build ./build
#RUN ls

# Package stage
FROM openjdk:latest
LABEL cicd="borsch3"

#RUN --mount=type=secret,id=NOTE_DB_USE db_user=/run/secrets/NOTE_DB_U

#RUN --mount=type=secret,id=NOTE_DB_PAS db_pass=/run/secrets/NOTE_DB_P

ARG JAR_FILE=Borsch-0.0.1-SNAPSHOT.jar

COPY /build/libs/${JAR_FILE} app.jar
EXPOSE 2665
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app.jar ${0} ${@}"]
