package com.nsoroma.trackermonitoring.restcontrollers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nsoroma.trackermonitoring.model.schedule.Schedule;
import com.nsoroma.trackermonitoring.scheduler.service.ScheduleBuild;
import com.nsoroma.trackermonitoring.scheduler.service.ScheduleService;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import javax.servlet.http.HttpServletRequest;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2019-09-12T18:13:17.843Z")

@Controller
public class SchedulesApiController implements SchedulesApi {

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    ScheduleBuild scheduleBuild;

    @Autowired
    Scheduler scheduler;

    @Autowired
    ScheduleService scheduleService;

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

    public JobDetail buildScheduleDetail(Schedule schedule) {
        return scheduleBuild.buildScheduleDetail(schedule);
    }

    public Trigger buildScheduleTrigger(JobDetail jobDetail, ZonedDateTime dateTime) {
        return scheduleBuild.buildScheduleTrigger(jobDetail, dateTime);
    }

    public Date scheduleJob(JobDetail jobDetail, Trigger trigger) throws SchedulerException {
        return scheduler.scheduleJob(jobDetail,trigger);
    }

    public  List<Schedule> getSchedules() throws SchedulerException {
        return scheduleService.getSchedules();
    }
}