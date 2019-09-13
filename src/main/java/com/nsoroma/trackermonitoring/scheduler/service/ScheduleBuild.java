package com.nsoroma.trackermonitoring.scheduler.service;

import com.nsoroma.trackermonitoring.model.schedule.Schedule;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

@Service
public class ScheduleBuild {

    public JobDetail buildScheduleDetail(Schedule schedule) {
        JobDataMap jobDataMap = new JobDataMap();

        jobDataMap.put("email", schedule.getEmail());
        jobDataMap.put("subject", "Tracker Monitoring Alert");
        jobDataMap.put("body", "Please find attach a data interval file");
        jobDataMap.put("scheduleId", schedule.getScheduleId());
        jobDataMap.put("alertFrequency",schedule.getAlertFrequency());
        jobDataMap.put("alertTime", schedule.getAlertTime());
        jobDataMap.put("lastUpdateDate", schedule.getLastUpdateDate());
        jobDataMap.put("trackerType", schedule.getTrackerType());
        jobDataMap.put("customerId", schedule.getCustomerId());

        return JobBuilder.newJob(Email.class)
                .withIdentity(UUID.randomUUID().toString(), "scheduled")
                .withDescription("execute schedule for trackers")
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    public Trigger buildScheduleTrigger(JobDetail jobDetail, ZonedDateTime startTime) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), "schedule-triggers")
                .withDescription("trigger schedule")
                .startAt(Date.from(startTime.toInstant()))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                .build();
    }
}
