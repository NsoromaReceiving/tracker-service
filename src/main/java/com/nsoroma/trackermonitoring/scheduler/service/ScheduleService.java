package com.nsoroma.trackermonitoring.scheduler.service;

import com.nsoroma.trackermonitoring.model.schedule.Schedule;
import org.quartz.SchedulerException;

import java.util.List;

public interface ScheduleService {

    List<Schedule> getSchedules() throws SchedulerException;

    Schedule getSchedule(String id) throws SchedulerException;

    Boolean deleteSchedule(String id) throws SchedulerException;

    Boolean updateSchedule(String id, Schedule schedule) throws SchedulerException;
}
