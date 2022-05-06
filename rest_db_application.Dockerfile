FROM openjdk:18
ARG JAR_FILE=target/*.jar
ADD ${JAR_FILE} app.jar
EXPOSE 8722
ENTRYPOINT ["java","-jar","/app.jar"]

