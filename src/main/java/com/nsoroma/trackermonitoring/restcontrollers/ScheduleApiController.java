package com.nsoroma.trackermonitoring.restcontrollers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nsoroma.trackermonitoring.model.schedule.Schedule;
import com.nsoroma.trackermonitoring.scheduler.service.ScheduleClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2019-09-12T18:13:17.843Z")

@Controller
public class ScheduleApiController implements ScheduleApi {

    @Autowired
    ScheduleClient scheduleClient;

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public ScheduleApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @Override
    public Optional<ObjectMapper> getObjectMapper() {
        return Optional.ofNullable(objectMapper);
    }

    @Override
    public Optional<HttpServletRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public Schedule getSchedule(String id) {
        return scheduleClient.getSchedule(id);
    }

    @Override
    public  Boolean deleteSchedule(String id) {
        return scheduleClient.deleteSchedule(id);
    }

    @Override
    public  Boolean updateSchedule(String id, Schedule schedule) {
        return scheduleClient.updateSchedule(id, schedule);
    }

}
