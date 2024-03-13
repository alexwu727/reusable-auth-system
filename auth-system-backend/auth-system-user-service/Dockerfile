FROM maven:3.8.1-openjdk-17-slim AS build
WORKDIR /app
COPY src /app/src
COPY pom.xml /app
RUN mvn -f /app/pom.xml clean package -DskipTests -Pcontainer

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar /app/app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]

#FROM openjdk:17-jdk-slim
#LABEL authors="alexwu"
#VOLUME /tmp
#EXPOSE 8080
#COPY target/auth-system-user-service-0.0.1-SNAPSHOT.jar /app.jar
#ENTRYPOINT ["java","-jar","/app.jar"]
# docker build -t auth-system-user-service -v /var/lib/docker/volumes/auth-system-user-service/_data:/tmp .