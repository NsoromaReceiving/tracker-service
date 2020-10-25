package com.nsoroma.trackermonitoring.restcontrollers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.nsoroma.trackermonitoring.model.trackerstate.TrackerState;
import com.nsoroma.trackermonitoring.services.Trackers;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class TrackerApiTest {

    @InjectMocks
    TrackerApi trackerApi;

    private static ObjectMapper objectMapper;
    private static MockHttpServletRequest httpServletRequest;
    private Trackers trackers;

    public TrackerApiTest(TrackerApi trackerApi) {
        this.trackerApi = trackerApi;
    }

    @Before
    public void setUp() throws Exception {
        trackers = mock(Trackers.class);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getTrackerByTrackerIDWithIdLength6() throws IOException, UnirestException {
        String trackerId = "123456";
        httpServletRequest.removeHeader("Accept");
        httpServletRequest.addHeader("Accept", "application/json");
        TrackerState trackerState = new TrackerState();
        trackerState.setTrackerId(trackerId);
        when(trackers.getServerTwoTracker(trackerId)).thenReturn(trackerState);

        ResponseEntity<TrackerState> responseEntity =  trackerApi.trackerID(trackerId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void getTrackerByTrackerIDWithIdLength6ButNotFound() throws IOException, UnirestException {
        String trackerId = "123456";
        httpServletRequest.removeHeader("Accept");
        httpServletRequest.addHeader("Accept", "application/json");
        TrackerState trackerState = new TrackerState();
        when(trackers.getServerTwoTracker(trackerId)).thenReturn(trackerState);

        ResponseEntity<TrackerState> responseEntity =  trackerApi.trackerID(trackerId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void getTrackerByTrackerIDWithIdLengthNot6() throws IOException, UnirestException {
        String trackerId = "1234567890";
        httpServletRequest.removeHeader("Accept");
        httpServletRequest.addHeader("Accept", "application/json");
        TrackerState trackerState = new TrackerState();
        trackerState.setTrackerId(trackerId);
        when(trackers.getServerOneTracker(trackerId)).thenReturn(Optional.of(trackerState));

        ResponseEntity<TrackerState> responseEntity =  trackerApi.trackerID(trackerId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(EqualsBuilder.reflectionEquals(trackerState, responseEntity.getBody()));
    }

    @Test
    public void getTrackerByTrackerIDWithIdLengthNot6ButNotFound() throws IOException, UnirestException {
        String trackerId = "1234567890";
        httpServletRequest.removeHeader("Accept");
        httpServletRequest.addHeader("Accept", "application/json");
        TrackerState trackerState = new TrackerState();
        when(trackers.getServerOneTracker(trackerId)).thenReturn(Optional.empty());

        ResponseEntity<TrackerState> responseEntity =  trackerApi.trackerID(trackerId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void returnStatus406WhenAcceptHeaderIsNotPresentInTrackerId() {
        String trackerId = "testTrackerId";
        httpServletRequest.removeHeader("Accept");

        ResponseEntity<TrackerState> responseEntity =  trackerApi.trackerID(trackerId);

        assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
    }

    @Test
    public void returnStatus406WhenAcceptHeaderFormatIsNotApplicationJsonInTrackerId() {
        String trackerId = "testTrackerId";
        httpServletRequest.removeHeader("Accept");
        httpServletRequest.addHeader("Accept", "text-plain");

        ResponseEntity<TrackerState> responseEntity =  trackerApi.trackerID(trackerId);

        assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
    }

    @Test
    public void returnStatus501WhenAnErrorOccursInServiceForTrackerId() throws IOException, UnirestException{
        String trackerId = "123456";
        httpServletRequest.removeHeader("Accept");
        httpServletRequest.addHeader("Accept", "application/json");

        when(trackers.getServerTwoTracker(trackerId)).thenThrow(UnirestException.class);

        ResponseEntity<TrackerState> responseEntity = trackerApi.trackerID(trackerId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void getTrackerByTrackerImei() throws IOException, UnirestException {
        String trackerImei = "testTrackerImei";
        httpServletRequest.removeHeader("Accept");
        httpServletRequest.addHeader("Accept", "application/json");
        TrackerState trackerState = new TrackerState();
        when(trackers.getTrackerByImei(trackerImei)).thenReturn(trackerState);

        ResponseEntity<TrackerState> responseEntity =  trackerApi.trackerByImei(trackerImei);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(EqualsBuilder.reflectionEquals(trackerState, responseEntity.getBody()));
    }

    @Test
    public void returnStatus406WhenAcceptHeaderIsNotPresentInTrackerByImei() {
        String trackerImei = "testTrackerImei";
        httpServletRequest.removeHeader("Accept");

        ResponseEntity<TrackerState> responseEntity =  trackerApi.trackerByImei(trackerImei);

        assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
    }

    @Test
    public void returnStatus406WhenAcceptHeaderFormatIsNotApplicationJsonInTrackerByImei() {
        String trackerImei = "testTrackerImei";
        httpServletRequest.removeHeader("Accept");
        httpServletRequest.addHeader("Accept", "xml");

        ResponseEntity<TrackerState> responseEntity =  trackerApi.trackerByImei(trackerImei);

        assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
    }

    @Test
    public void returnStatus501WhenAnErrorOccursInServiceForTrackerByImei() throws IOException, UnirestException{
        String trackerImei = "testTrackerImei";
        httpServletRequest.removeHeader("Accept");
        httpServletRequest.addHeader("Accept", "application/json");

        when(trackers.getTrackerByImei(trackerImei)).thenThrow(UnirestException.class);

        ResponseEntity<TrackerState> responseEntity = trackerApi.trackerByImei(trackerImei);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void checkObjectMapper() {
        Optional<ObjectMapper> objectMapper = trackerApi.getObjectMapper();
        assertTrue(objectMapper.isPresent());
    }


    @Parameterized.Parameters
    public static Collection<Object[]> instancesToTest() {
        objectMapper = new ObjectMapper();
        httpServletRequest = new MockHttpServletRequest();
        return Collections.singletonList(
                new Object[]{new TrackerApiController(objectMapper, httpServletRequest)}
        );
    }
}