FROM maven:3.8.2-openjdk-17-slim AS build

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src src
COPY .git .git
RUN mvn package

FROM openjdk:17-jdk-slim
COPY --from=build /target/callback-consumer.jar callback-consumer.jar
EXPOSE 8080
CMD java -jar callback-consumer.jar
