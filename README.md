
# Nsoroma Tracker Monitoring Service #

## Quick summary ##

This spring-boot (maven) application exposes RESRT API end points to fetch data on tracker states for both server one (1) and server two (2)


## Version

* Spring Boot 2.1.7
* Spring Cloud 1.1.7


## Execution instructions ##

The application starts as a normal Spring Boot application:

* Run `mvn clean spring-boot:run` inside the proeject

## API Documentation
Working URLEndPoints.
1. **Get TrackerState List** `api/trackers?startDate={value}&endDate={value}&customerId={value}&status={value}&server={value}`
    * ***startDate*** : start date for data interval
    * ***endDate*** : end date for data interval
    * ***customerId*** : Id of a particular customer to fetch trackers registered to that customer
    * ***status*** : trackers with the specified status. these are; *online*, *offline*, *signal_lost*, *idle*, and *just_registered*
    * ***server*** : trackers limited to server one(1) and or server two (2)
2. **Get TrackerState with tracker id** `api/tracker/{395940}`.

## Service Architecture
1. **Datasource**: The service fetches data from two providers server one (3d tracking) and server two (navixy). with credentials in the application.yml file

* There are two respective clients for that purpose found in `src/main/java/nsoroma/trackermonitoring/datasourceclient` package

* The last state of trackers are fetched using schedulers and stored intermitently using schedulers that run in the background found in `src/main/java/nsoroma/trackermonitoring/services/BackgroundTask.java` class

2. **Data Storage**: The backgorund scheduler fetches and stores the tracker last tracker state in a mongodb database with data repository found in `src/main/java/nsoroma/trackermonitoring/repository`

3. **Exception** Custom exceptions are handled in `src/main/java/nsoroma/trackermonitoring/exceptions` package

## Depolyment Strategy
The application is dockerised with a docker file using java 8 found in a root folder.