package com.nsoroma.trackermonitoring.services;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.nsoroma.trackermonitoring.exceptions.DataSourceClientResponseException;
import com.nsoroma.trackermonitoring.model.trackerstate.TrackerState;
import com.nsoroma.trackermonitoring.repository.TrackerStateRepository;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashSet;

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
        when(trackers.getServerTwoTrackerStates()).thenReturn(trackersList);
        backgroundTask.getNewTrackerStates();
        verify(trackerStateRepository).saveAll(trackersList);
    }

    @Test
    public void getServer1Tracker() throws IOException, UnirestException, DataSourceClientResponseException {
        TrackerState trackerState = new TrackerState();
        trackerState.setTrackerId("testTrackerId");
        LinkedHashSet<TrackerState> trackersList = new LinkedHashSet<>(Collections.singletonList(trackerState));
        when(trackers.getServerOneTrackerStates()).thenReturn(trackersList);
        backgroundTask.getServer1Trackers();
        verify(trackerStateRepository).saveAll(trackersList);
    }
}