package com.nsoroma.trackermonitoring.scheduler.service;

import com.nsoroma.trackermonitoring.model.schedule.Schedule;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService{

    @Autowired
    private Scheduler scheduler;
    @Override
    public List<Schedule> getSchedules() throws SchedulerException {
        List<Schedule> scheduleList = new ArrayList<>();
        for( String groupName: scheduler.getJobGroupNames()) {
            for(JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                Schedule schedule = new Schedule();
                JobDetail scheduleDetail = scheduler.getJobDetail(jobKey);
                schedule.setCustomerId(scheduleDetail.getJobDataMap().getString("customerId"));
                schedule.setEmail(scheduleDetail.getJobDataMap().getString("email"));
                schedule.setAlertFrequency(scheduleDetail.getJobDataMap().getString("alertFrequency"));
                schedule.setAlertTime(LocalDateTime.parse(scheduleDetail.getJobDataMap().getString("alertTime")));
                schedule.setLastUpdateDate(scheduleDetail.getJobDataMap().getString("lastUpdateDate"));
                schedule.setZoneId(ZoneId.of(scheduleDetail.getJobDataMap().getString("zoneId")));
                schedule.setStatus(scheduleDetail.getJobDataMap().getString("status"));
                schedule.setTrackerType(scheduleDetail.getJobDataMap().getString("trackerType"));
                schedule.setScheduleId(scheduleDetail.getJobDataMap().getString("scheduleId"));
                scheduleList.add(schedule);
            }
        }
        return scheduleList;
    }
}
