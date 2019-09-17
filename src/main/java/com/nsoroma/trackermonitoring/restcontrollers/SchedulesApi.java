/**
 * NOTE: This class is auto generated by the swagger code generator program (2.4.8).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package com.nsoroma.trackermonitoring.restcontrollers;

import com.nsoroma.trackermonitoring.model.schedule.Schedule;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Date;
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

    public JobDetail buildScheduleDetail(Schedule schedule);

    public Trigger buildScheduleTrigger(JobDetail jobDetail, ZonedDateTime dateTime);

    public Date scheduleJob(JobDetail jobDetail, Trigger trigger) throws SchedulerException;

    public List<Schedule> getSchedules() throws SchedulerException;

    @ApiOperation(value = "creates a new schedule", nickname = "newSchedule", notes = "creates a new schedule to alert the subscriber based on the schedule settings", tags={ "developers", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success! schedule created"),
        @ApiResponse(code = 406, message = "Not Acceptable") })
    @RequestMapping(value = "/schedules",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    default ResponseEntity<Void> newSchedule(@ApiParam(value = "", required = true) @Valid @RequestBody Schedule schedule) {
        try {
            if(getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
                ZonedDateTime dateTime = ZonedDateTime.of(schedule.getAlertTime(), schedule.getZoneId());
                if(dateTime.isBefore(ZonedDateTime.now())) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                JobDetail jobDetail = buildScheduleDetail(schedule);
                Trigger trigger = buildScheduleTrigger(jobDetail, dateTime);
                scheduleJob(jobDetail,trigger);

                return new ResponseEntity<>(HttpStatus.OK);

            } else {
                log.warn("ObjectMapper or HttpServletRequest not configured in default SchedulesApi interface so no example is generated");
            }
            return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ApiOperation(value = "returns a list of schedule-profiles", nickname = "scheduleProfiles", notes = "returns all schedule-profiles created for the nsoroma tracker monitoring system", response = Schedule.class, responseContainer = "List", tags={ "developers", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "success! list of all schedule profiles", response = Schedule.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "bad input parameter") })
    @RequestMapping(value = "/schedules",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.GET)
    default ResponseEntity<List<Schedule>> scheduleProfiles() throws SchedulerException {
        if(getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
            if (getAcceptHeader().get().contains("application/json")) {
                return new ResponseEntity<>(getSchedules(), HttpStatus.OK);
            }
        } else {
            log.warn("ObjectMapper or HttpServletRequest not configured in default SchedulesApi interface so no example is generated");
        }
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

}
