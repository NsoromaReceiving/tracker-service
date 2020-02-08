package com.nsoroma.trackermonitoring.restcontrollers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.nsoroma.trackermonitoring.model.trackerstate.TrackerState;
import com.nsoroma.trackermonitoring.services.Trackers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(Parameterized.class)
public class TrackersApiTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @InjectMocks
    private TrackersApi trackersApi;

    private static ObjectMapper objectMapper;
    private static MockHttpServletRequest httpServletRequest;
    private Trackers trackers;

    public TrackersApiTest(TrackersApi trackersApi) {
        this.trackersApi = trackersApi;

    }

    @Before
    public void setup() {
        trackers = mock(Trackers.class);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void checkAllTrackersAreReturn() throws IOException, UnirestException {
        httpServletRequest.removeHeader("Accept");
        httpServletRequest.addHeader("Accept", "application/json");
        LinkedHashSet<TrackerState> trackerStates = new LinkedHashSet<>();
        when(trackers.getTrackers(Optional.empty(), Optional.empty(), Optional.empty(),
              Optional.empty(), Optional.empty(), Optional.empty())).thenReturn(trackerStates);

        ResponseEntity<LinkedHashSet<TrackerState>> responseEntity =  trackersApi.trackers(Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void returnStatus406WhenAcceptHeaderIsNotPresent() throws IOException, UnirestException {
        httpServletRequest.removeHeader("Accept");

        ResponseEntity<LinkedHashSet<TrackerState>> responseEntity =  trackersApi.trackers(Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());

        assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
    }

    @Test
    public void returnStatus406WhenAcceptHeaderFormatIsNotApplicationJson() throws IOException, UnirestException {
        httpServletRequest.removeHeader("Accept");
        httpServletRequest.addHeader("Accept", "xml");

        ResponseEntity<LinkedHashSet<TrackerState>> responseEntity =  trackersApi.trackers(Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());

        assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
    }

    @Test
    public void returnStatus501WhenAnErrorOccursInService() throws IOException, UnirestException{
        httpServletRequest.removeHeader("Accept");
        httpServletRequest.addHeader("Accept", "application/json");

        LinkedHashSet<TrackerState> trackerStates = new LinkedHashSet<>();
        when(trackers.getTrackers(Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty())).thenThrow(UnirestException.class);

        ResponseEntity<LinkedHashSet<TrackerState>> responseEntity = trackersApi.trackers(Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,responseEntity.getStatusCode());
    }


    @Test
    public void checkObjectMapper() {
        Optional<ObjectMapper> objectMapper = trackersApi.getObjectMapper();
        assertTrue(objectMapper.isPresent());
    }

    @Parameterized.Parameters
    public static Collection<Object[]> instancesToTest() {
        objectMapper = new ObjectMapper();
        httpServletRequest = new MockHttpServletRequest();
        return Collections.singletonList(
                new Object[]{new TrackersApiController(objectMapper, httpServletRequest)}
        );
    }
}