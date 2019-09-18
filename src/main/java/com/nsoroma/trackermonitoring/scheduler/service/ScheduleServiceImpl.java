package com.nsoroma.trackermonitoring.scheduler.service;

import com.nsoroma.trackermonitoring.model.schedule.Schedule;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService{

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private ScheduleBuild scheduleBuild;

    @Override
    public List<Schedule> getSchedules() throws SchedulerException {
        List<Schedule> scheduleList = new ArrayList<>();
        for( String groupName: scheduler.getJobGroupNames()) {
            for(JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                Schedule schedule = new Schedule();
                scheduleList.add(setScheduleDetails(schedule,jobKey));
            }
        }
        return scheduleList;
    }

    @Override
    public Schedule getSchedule(String id) throws SchedulerException{
        Schedule schedule = new Schedule();
        JobKey jobKey = new JobKey(id, "NsoromaTrackerMonitoringSystemJobs");

        return setScheduleDetails(schedule, jobKey);
    }

    @Override
    public Boolean deleteSchedule(String id) throws SchedulerException {
        JobKey jobKey = new JobKey(id, "NsoromaTrackerMonitoringSystemJobs");
        return scheduler.deleteJob(jobKey);
    }

    @Override
    public Boolean updateSchedule(String id, Schedule schedule) throws SchedulerException {

        ZonedDateTime dateTime = ZonedDateTime.of(schedule.getAlertTime(), schedule.getZoneId());

        if(dateTime.isBefore(ZonedDateTime.now())) {
            return false;
        } else {
            JobKey jobKey = new JobKey(id, "NsoromaTrackerMonitoringSystemJobs");
            TriggerKey triggerKey = TriggerKey.triggerKey(id, "NsoromaTrackerMonitoringSystemTriggers");

            JobDetail jobDetails = scheduler.getJobDetail(jobKey);

            Trigger trigger = scheduler.getTrigger(triggerKey);

            trigger = trigger.getTriggerBuilder().startAt(Date.from(dateTime.toInstant())).build();

            scheduler.rescheduleJob(triggerKey, trigger);
            scheduler.addJob(updateJobDetails(jobDetails, schedule),true);

            return true;
        }

    }


    /**** helper functions ****/

    private Schedule setScheduleDetails(Schedule schedule, JobKey jobKey) throws SchedulerException{
        System.out.println(jobKey.getName());
        System.out.println((jobKey.getGroup()));
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
        schedule.setSubject(scheduleDetail.getJobDataMap().getString("subject"));

        return schedule;
    }

    private JobDetail updateJobDetails(JobDetail jobDetails, Schedule schedule) {
        jobDetails.getJobDataMap().put("email", schedule.getEmail());
        jobDetails.getJobDataMap().put("subject", schedule.getSubject());
        jobDetails.getJobDataMap().put("body", "Please find attached the data intervals for filter parameters: / " +
                "Last Update Date : " + schedule.getLastUpdateDate() +
                " / Tracker Type : " + schedule.getTrackerType() +
                " / Customer Id : " + schedule.getCustomerId() +
                " / Status : " + schedule.getStatus());
        jobDetails.getJobDataMap().put("alertFrequency",schedule.getAlertFrequency());
        jobDetails.getJobDataMap().put("alertTime", schedule.getAlertTime().toString());
        jobDetails.getJobDataMap().put("zoneId", schedule.getZoneId().toString());
        jobDetails.getJobDataMap().put("lastUpdateDate", schedule.getLastUpdateDate());
        jobDetails.getJobDataMap().put("trackerType", schedule.getTrackerType());
        jobDetails.getJobDataMap().put("customerId", schedule.getCustomerId());
        jobDetails.getJobDataMap().put("status", schedule.getStatus());

        return jobDetails;
    }
}
