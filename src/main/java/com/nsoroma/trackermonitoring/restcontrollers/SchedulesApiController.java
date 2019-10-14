package com.nsoroma.trackermonitoring.restcontrollers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nsoroma.trackermonitoring.model.schedule.Schedule;
import com.nsoroma.trackermonitoring.scheduler.service.ScheduleClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2019-09-12T18:13:17.843Z")

@Controller
public class SchedulesApiController implements SchedulesApi {

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    ScheduleClient scheduleClient;

    @org.springframework.beans.factory.annotation.Autowired
    public SchedulesApiController(ObjectMapper objectMapper, HttpServletRequest request) {
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

    public Boolean scheduleJob(Schedule schedule) {
        return scheduleClient.createSchedule(schedule);
    }

    public  List<Schedule> getSchedules() {
        return scheduleClient.getScheduleList();
    }
}
