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
RUN --mount=type=secret,id=NOTE_DB_U db_user=`cat /run/secrets/NOTE_DB_U`
RUN echo ${db_user}
RUN --mount=type=secret,id=db_pass db_pass=`cat /run/secrets/NOTE_DB_P`
RUN echo ${db_pass}
#RUN cat /run/secrets/NOTE_DB_P
#VOLUME /tmp
ARG JAR_FILE=Borsch-0.0.1-SNAPSHOT.jar
#ENV APP_HOME=/usr/app/
#WORKDIR $APP_HOME
#COPY --from=BUILD $APP_HOME .
#RUN ls
COPY /build/libs/${JAR_FILE} app.jar

#COPY /usr/app/build/libs/${JAR_FILE} app.jar
EXPOSE 2665
ENV spring.profiles.active=production
#ENV NOTE_DB_USER=${db_user}
#ENV NOTE_DB_PASSWORD=${db_pass}
#CMD sh -c echo $NOTE_DB_PASSWORD
CMD env
#RUN echo NOTE_DB_PASSWORD
#RUN ECHO ${NOTE_DB_PASSWORD}
#ENTRYPOINT ["/bin/sh", "-c", "export NOTE_DB_USER=`cat /run/secrets/NOTE_DB_U`"]
#RUN ["/bin/sh", "-c", "ECHO ${NOTE_DB_PASSWORD}"]
#ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app.jar ${0} ${@}"]
