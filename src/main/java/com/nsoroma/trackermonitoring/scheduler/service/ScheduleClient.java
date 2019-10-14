package com.nsoroma.trackermonitoring.scheduler.service;

import com.nsoroma.trackermonitoring.model.schedule.Schedule;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient("JobScheduler")
public interface ScheduleClient {

    @RequestMapping(method = RequestMethod.GET, value = "/api/schedules")
    List<Schedule> getScheduleList();

    @RequestMapping(method = RequestMethod.POST, value = "/api/schedules")
    boolean createSchedule(Schedule schedule);

    @RequestMapping(method = RequestMethod.GET, value = "/api/schedule/{id}")
    Schedule getSchedule(@PathVariable("id") String id);

    @RequestMapping(method = RequestMethod.DELETE, value = "/api/schedule/{id}")
    Boolean deleteSchedule(@PathVariable("id") String id);

    @RequestMapping(method = RequestMethod.PUT, value = "/api/schedule/{id}")
    Boolean updateSchedule(@PathVariable("id") String id, Schedule schedule);

}
