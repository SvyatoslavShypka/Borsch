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
RUN --mount=type=secret,id=aws AWS_SHARED_CREDENTIALS_FILE=/run/secrets/NOTE_DB_PASSWORD
#VOLUME /tmp
ARG JAR_FILE=Borsch-0.0.1-SNAPSHOT.jar
#ENV APP_HOME=/usr/app/
#WORKDIR $APP_HOME
#COPY --from=BUILD $APP_HOME .
#RUN ls
COPY /build/libs/${JAR_FILE} app.jar

#COPY /usr/app/build/libs/${JAR_FILE} app.jar
EXPOSE 2665
RUN EXPORT NOTE_DB_PASSWORD=${AWS_SHARED_CREDENTIALS_FILE}
RUN ECHO ${NOTE_DB_PASSWORD}
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app.jar ${0} ${@}"]
