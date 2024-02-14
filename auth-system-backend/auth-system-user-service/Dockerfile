FROM openjdk:17-jdk-slim
LABEL authors="alexwu"
VOLUME /tmp
EXPOSE 8080
COPY target/auth-system-user-service-0.0.1-SNAPSHOT.jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
# docker build -t auth-system-user-service -v /var/lib/docker/volumes/auth-system-user-service/_data:/tmp .