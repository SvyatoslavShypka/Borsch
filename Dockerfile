FROM openjdk:latest
LABEL cicd="borsch3"
VOLUME /tmp
ARG JAR_FILE
COPY build/libs/${JAR_FILE} app.jar
EXPOSE 2665
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app.jar ${0} ${@}"]