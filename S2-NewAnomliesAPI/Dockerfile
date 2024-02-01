FROM openjdk:17-jdk-alpine
EXPOSE 8081

ADD target/*.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar", "server.address=127.0.0.1"]
