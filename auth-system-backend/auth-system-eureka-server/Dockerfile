FROM openjdk:17-jdk-slim
LABEL authors="alexwu"
COPY target/auth-system-eureka-server-0.0.1-SNAPSHOT.jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]