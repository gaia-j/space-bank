FROM ubuntu:latest as build

WORKDIR /app

COPY . .

RUN mvn clean install -X

FROM openjdk:17-alpine

EXPOSE 8080

COPY --from=build /app/target/deploy_render_1.0.0.jar /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]