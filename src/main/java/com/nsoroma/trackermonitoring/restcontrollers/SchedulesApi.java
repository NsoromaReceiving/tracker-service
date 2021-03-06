/**
 * NOTE: This class is auto generated by the swagger code generator program (2.4.8).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package com.nsoroma.trackermonitoring.restcontrollers;

import com.nsoroma.trackermonitoring.model.schedule.Schedule;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2019-09-12T18:13:17.843Z")

@Api(value = "schedules", description = "the schedules API")
public interface SchedulesApi {

    Logger log = LoggerFactory.getLogger(SchedulesApi.class);

    default Optional<ObjectMapper> getObjectMapper() {
        return Optional.empty();
    }

    default Optional<HttpServletRequest> getRequest() {
        return Optional.empty();
    }

    default Optional<String> getAcceptHeader() {
        return getRequest().map(r -> r.getHeader("Accept"));
    }

    public Boolean scheduleJob(Schedule schedule);

    public List<Schedule> getSchedules();

    @ApiOperation(value = "creates a new schedule", nickname = "newSchedule", notes = "creates a new schedule to alert the subscriber based on the schedule settings", tags={ "developers", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success! schedule created"),
        @ApiResponse(code = 406, message = "Not Acceptable") })
    @PostMapping(value = "/api/schedules", consumes = { "application/json" })
    @CrossOrigin
    default ResponseEntity<Void> newSchedule(@ApiParam(value = "", required = true) @Valid @RequestBody Schedule schedule) {
        try {
            if(getAcceptHeader().isPresent()) {
                ZonedDateTime dateTime = ZonedDateTime.of(schedule.getAlertTime(), schedule.getZoneId());
                if(dateTime.isBefore(ZonedDateTime.now())) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }

                if(scheduleJob(schedule)) {
                    return new ResponseEntity<>(HttpStatus.MULTI_STATUS.CREATED.OK);
                } else {
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }

            } else {
                log.warn("ObjectMapper or HttpServletRequest not configured in default SchedulesApi interface so no example is generated");
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        } catch (Exception e) {
            log.error("Unexpected error creating schedule");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @ApiOperation(value = "returns a list of schedule-profiles", nickname = "scheduleProfiles", notes = "returns all schedule-profiles created for the nsoroma tracker monitoring system", response = Schedule.class, responseContainer = "List", tags={ "developers", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "success! list of all schedule profiles", response = Schedule.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "bad input parameter") })
    @GetMapping(value = "/api/schedules", produces = { "application/json" })
    @CrossOrigin
    default ResponseEntity<List<Schedule>> scheduleProfiles() {
        if(getAcceptHeader().isPresent()) {
            if (getAcceptHeader().get().contains("application/json")) {
                return new ResponseEntity<>(getSchedules(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        } else {
            log.warn("ObjectMapper or HttpServletRequest not configured in default SchedulesApi interface so no example is generated");
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

}
