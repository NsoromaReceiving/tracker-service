
# Nsoroma Tracker Monitoring Service #

## Quick summary ##

This application exposes RESRT API end points  to monitor trackers for the Nsoroma Team.

It also registers as spring cloud eureka client on this : [Eureka-Server] (https://banabasave@bitbucket.org/banabasave/tracker-monitoring-eureka-server.git) in order to be acessible by the [tracker-monitoring-scheduler] (https://banabasave@bitbucket.org/hawkmanlabs/tracker-monitoring-scheduler.git)

It uses [Spring Boot](http://projects.spring.io/spring-boot/) to start Spring context and run the application and [Spring Cloud Eureka](https://cloud.spring.io/spring-cloud-netflix/) to integrate Netflix implementation into Spring.

##Version

* Spring Boot 2.1.7
* Spring Cloud 1.1.7


### Execution instructions ###

The application starts as a normal Spring Boot application:

* Run `mvn clean spring-boot:run` inside the proeject

## Test API Locally
Working URLEndPoints.
1. Get TrackerState List `localhost:/api/trackers`.
2. Get TrackerState List for Customer `localhost:/api/trackers/?customerId=105478`.
3. Get TrackerState List for with tracker-type `localhost:/api/trackers/?type=bce_fms500_light_vt`.
4. Get TrackerState with tracker id `localhost:/api/tracker/{395940}`.