FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Dspring.profiles.active=Integrated","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]