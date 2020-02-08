package com.nsoroma.trackermonitoring.services;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.nsoroma.trackermonitoring.model.trackerstate.TrackerState;
import com.nsoroma.trackermonitoring.repository.TrackerStateRepository;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BackgroundTaskTest {

    private BackgroundTask backgroundTask;
    private Trackers trackers;
    private TrackerStateRepository trackerStateRepository;


    @Before
    public void setUp() throws Exception {
        backgroundTask = new BackgroundTask();
        trackers = mock(Trackers.class);
        trackerStateRepository = mock(TrackerStateRepository.class);

        backgroundTask.setTrackers(trackers);
        backgroundTask.setTrackerStateRepository(trackerStateRepository);
    }

    @Test
    public void getNewTrackerStates() throws IOException, UnirestException {
        TrackerState trackerState = new TrackerState();
        trackerState.setTrackerId("testTrackerId");
        LinkedHashSet<TrackerState> trackersList = new LinkedHashSet<>(Collections.singletonList(trackerState));
        when(trackers.getAllTrackerStates()).thenReturn(trackersList);
        backgroundTask.getNewTrackerStates();
        verify(trackerStateRepository).saveAll(trackersList);
    }
}