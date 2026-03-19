From gradle:9.0.0-jdk21 AS build

WORKDIR /app

COPY --chown=gradle:gradle . .

RUN gradle clean build -x test

FROM alpine/java:21-jre

WORKDIR /app

COPY --from=build /app/build/libs/*-SNAPSHOT.jar app.jar

CMD ["java", "-jar", "app.jar"]