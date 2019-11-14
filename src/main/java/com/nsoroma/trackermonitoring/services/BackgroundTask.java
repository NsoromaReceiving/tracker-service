package com.nsoroma.trackermonitoring.services;

import com.nsoroma.trackermonitoring.model.trackerstate.TrackerState;
import com.nsoroma.trackermonitoring.repository.TrackerStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;

@Component
public class BackgroundTask {

    @Autowired
    private Trackers trackers;

    @Autowired
    private TrackerStateRepository trackerStateRepository;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Scheduled(fixedDelay = 100000)
    public void getNewTrackerStates() throws IOException {
        System.out.println("Getting Tracker State Updates at - " + formatter.format(LocalDateTime.now()));
        LinkedHashSet<TrackerState> trackersList =  trackers.getAllTrackerStates();
        System.out.println(trackersList);
        trackerStateRepository.saveAll(trackersList);
    }
}
