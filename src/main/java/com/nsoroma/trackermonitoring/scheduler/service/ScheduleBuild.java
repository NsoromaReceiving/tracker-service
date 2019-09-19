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

        if(schedule.getScheduleId() == null) { // to enable reuse in in updating a schedule
            String scheduleId = UUID.randomUUID().toString();
            schedule.setScheduleId(scheduleId);
        }

        System.out.println(schedule.getScheduleId());

        jobDataMap.put("email", schedule.getEmail());
        jobDataMap.put("subject", schedule.getSubject());
        jobDataMap.put("body", "Please find attached the data intervals for filter parameters: / " +
                "Last Update Date : " + schedule.getLastUpdateDate() +
                " / Tracker Type : " + schedule.getTrackerType() +
                " / Customer Id : " + schedule.getCustomerId() +
                " / Status : " + schedule.getStatus());
        jobDataMap.put("scheduleId", schedule.getScheduleId());
        jobDataMap.put("alertFrequency",schedule.getAlertFrequency());
        jobDataMap.put("alertTime", schedule.getAlertTime().toString());
        jobDataMap.put("zoneId", schedule.getZoneId().toString());
        jobDataMap.put("lastUpdateDate", schedule.getLastUpdateDate());
        jobDataMap.put("trackerType", schedule.getTrackerType());
        jobDataMap.put("customerId", schedule.getCustomerId());
        jobDataMap.put("status", schedule.getStatus());

        return JobBuilder.newJob(Email.class)
                .withIdentity(schedule.getScheduleId(), "NsoromaTrackerMonitoringSystemJobs")
                .withDescription("execute schedules for the tracker monitoring system")
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    public Trigger buildScheduleTrigger(JobDetail jobDetail, ZonedDateTime startTime) {

        String alertFrequency = jobDetail.getJobDataMap().getString("alertFrequency");

        if (alertFrequency.equals("everyday")) {
            return TriggerBuilder.newTrigger()
                    .forJob(jobDetail)
                    .withIdentity(jobDetail.getKey().getName(), "NsoromaTrackerMonitoringSystemTriggers")
                    .withDescription("triggers for the Nsoroma tracker monitoring system")
                    .startAt(Date.from(startTime.toInstant()))
                    .withSchedule(SimpleScheduleBuilder.repeatMinutelyForever(2).withMisfireHandlingInstructionFireNow())
                    .build();
        } else {
            return TriggerBuilder.newTrigger()
                    .forJob(jobDetail)
                    .withIdentity(jobDetail.getKey().getName(), "NsoromaTrackerMonitoringSystemTriggers")
                    .withDescription("triggers for the Nsoroma tracker monitoring system")
                    .startAt(Date.from(startTime.toInstant()))
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                    .build();
        }

    }
}
