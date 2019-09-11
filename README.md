
## Clone this repository

1. git clone https://banabasave@bitbucket.org/hawkmanlabs/nsoroma-tracker-monitoring-service.git

## Run application locally

1. To application relies on another https://bitbucket.org/banabasave/nsoroma-config-service/src/master/ for configuration data.
2. Clone the config-server through the link above. and execute `mvn clean spring-boot:run` to start the config server.
3. execute `mvn clean spring-boot:run` to start server for this repository.

## Test API Locally
Working URLEndPoints.
1. Get TrackerState List `localhost://trackers`.
2. Get TrackerState List for Customer `localhost://trackers/?customerId=105478`.
3. Get TrackerState List for with tracker-type `localhost://trackers/?type=bce_fms500_light_vt`.
4. Get TrackerState with tracker id `localhost://tracker/{395940}`.