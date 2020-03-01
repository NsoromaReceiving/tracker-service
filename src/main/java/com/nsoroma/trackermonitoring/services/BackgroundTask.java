package com.nsoroma.trackermonitoring.services;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.nsoroma.trackermonitoring.exceptions.DataSourceClientResponseException;
import com.nsoroma.trackermonitoring.model.trackerstate.TrackerState;
import com.nsoroma.trackermonitoring.repository.TrackerStateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;

@Component
public class BackgroundTask {

    private Logger log = LoggerFactory.getLogger(BackgroundTask.class);


    @Autowired
    private Trackers trackers;

    @Autowired
    private TrackerStateRepository trackerStateRepository;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Scheduled(fixedDelay = 100000)
    public void getNewTrackerStates() throws IOException, UnirestException {
        String dateFormat = formatter.format(LocalDateTime.now());
        log.info("Getting Tracker State Updates at - {} ", dateFormat);
        LinkedHashSet<TrackerState> trackersList =  trackers.getServerTwoTrackerStates();
        String trackerListStr = String.valueOf(trackersList);
        log.info(trackerListStr);
        trackerStateRepository.saveAll(trackersList);
    }

    @Scheduled(fixedDelay = 100000)
    public void getServer1Trackers() throws IOException, UnirestException, DataSourceClientResponseException {
        LinkedHashSet<TrackerState> trackerStateList = trackers.getServerOneTrackerStates();
        log.info(String.valueOf(trackerStateList));
        trackerStateRepository.saveAll(trackerStateList);
    }

    public void setTrackers(Trackers trackers) {
        this.trackers = trackers;
    }

    public void setTrackerStateRepository(TrackerStateRepository trackerStateRepository) {
        this.trackerStateRepository = trackerStateRepository;
    }
}
