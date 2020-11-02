FROM maven:3.6.2-jdk-8-slim As Package

COPY src /usr/app/src

COPY pom.xml /usr/app

RUN mvn -f /usr/app/pom.xml clean package



FROM java:8-jdk-alpine

COPY --from=Package ./usr/app/target/nsoroma-tracker-monitoring-service-0.0.1-SNAPSHOT.jar /usr/app/

WORKDIR /usr/app

RUN sh -c 'touch nsoroma-tracker-monitoring-service-0.0.1-SNAPSHOT.jar'

##ENTRYPOINT ["java", "-Dspring.data.mongodb.uri=mongodb://mongocontainer/test", "-jar", "nsoroma-tracker-monitoring-service-0.0.1-SNAPSHOT.jar"]

ENTRYPOINT ["java", "-jar", "nsoroma-tracker-monitoring-service-0.0.1-SNAPSHOT.jar"]