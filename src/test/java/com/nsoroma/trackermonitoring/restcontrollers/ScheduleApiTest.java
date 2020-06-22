package com.nsoroma.trackermonitoring.restcontrollers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nsoroma.trackermonitoring.datasourceclient.api.model.Gps;
import com.nsoroma.trackermonitoring.model.customer.SlimCustomer;
import com.nsoroma.trackermonitoring.model.schedule.Schedule;
import com.nsoroma.trackermonitoring.model.trackerstate.TrackerState;
import com.nsoroma.trackermonitoring.scheduler.service.ScheduleClient;
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

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class ScheduleApiTest {

    @InjectMocks
    ScheduleApi scheduleApi;

    private static ObjectMapper objectMapper;
    private static MockHttpServletRequest httpServletRequest;
    private ScheduleClient scheduleClient;


    public ScheduleApiTest (ScheduleApi scheduleApi) {
        this.scheduleApi = scheduleApi;
    }

    @Before
    public void setUp() throws Exception {
        scheduleClient = mock(ScheduleClient.class);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void deleteScheduleById() {
        String scheduleId = "testScheduleId";
        httpServletRequest.removeHeader("Accept");
        httpServletRequest.addHeader("Accept", "application/json");

        when(scheduleClient.deleteSchedule(scheduleId)).thenReturn(true);

        ResponseEntity<Void> responseEntity =  scheduleApi.scheduleDelete(scheduleId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void getInternalServerErrorWhenScheduleClientFails() {
        String scheduleId = "testScheduleId";
        httpServletRequest.removeHeader("Accept");
        httpServletRequest.addHeader("Accept", "application/json");

        when(scheduleClient.deleteSchedule(scheduleId)).thenReturn(false);

        ResponseEntity<Void> responseEntity =  scheduleApi.scheduleDelete(scheduleId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void getNotAcceptableWhenAcceptHeaderIsNotPresent() {
        String scheduleId = "testScheduleId";
        httpServletRequest.removeHeader("Accept");

        ResponseEntity<Void> responseEntity =  scheduleApi.scheduleDelete(scheduleId);

        assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
    }

    @Test
    public void getScheduleByScheduleId() {
        String scheduleId = "testScheduleId";
        httpServletRequest.removeHeader("Accept");
        httpServletRequest.addHeader("Accept", "application/json");

        Schedule schedule = new Schedule();

        when(scheduleClient.getSchedule(scheduleId)).thenReturn(schedule);

        ResponseEntity<Schedule> responseEntity =  scheduleApi.scheduleId(scheduleId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(EqualsBuilder.reflectionEquals(schedule, responseEntity.getBody()));
    }

    @Test
    public void getNotAcceptableWhenAcceptHeaderNotPresent() {
        String scheduleId = "testScheduleId";
        httpServletRequest.removeHeader("Accept");

        ResponseEntity<Schedule> responseEntity =  scheduleApi.scheduleId(scheduleId);

        assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
    }

    @Test
    public void getNotAcceptableWhenAcceptHeaderFormatIsNotApplicationJson() {
        String scheduleId = "testScheduleId";
        httpServletRequest.removeHeader("Accept");
        httpServletRequest.addHeader("Accept", "xml");

        ResponseEntity<Schedule> responseEntity =  scheduleApi.scheduleId(scheduleId);

        assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
    }

    @Test
    public void updateScheduleByID() {
        String scheduleId = "testScheduleId";
        httpServletRequest.removeHeader("Accept");
        httpServletRequest.addHeader("Accept", "application/json");

        Schedule schedule = new Schedule();

        when(scheduleClient.updateSchedule(scheduleId, schedule)).thenReturn(true);

        ResponseEntity<Void> responseEntity =  scheduleApi.scheduleUpdate(scheduleId, schedule);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void getInternalServerErrorInUpdateScheduleByID() {
        String scheduleId = "testScheduleId";
        httpServletRequest.removeHeader("Accept");
        httpServletRequest.addHeader("Accept", "application/json");

        Schedule schedule = new Schedule();

        when(scheduleClient.updateSchedule(scheduleId, schedule)).thenReturn(false);

        ResponseEntity<Void> responseEntity =  scheduleApi.scheduleUpdate(scheduleId, schedule);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void getNotAcceptableInUpdateScheduleByIdWhenAcceptHeaderNotPresent() {
        String scheduleId = "testScheduleId";
        httpServletRequest.removeHeader("Accept");

        Schedule schedule = new Schedule();

        ResponseEntity<Void> responseEntity =  scheduleApi.scheduleUpdate(scheduleId, schedule);

        assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
    }

    @Test
    public void checkObjectMapper() {
        Optional<ObjectMapper> objectMapper = scheduleApi.getObjectMapper();
        assertTrue(objectMapper.isPresent());
    }

    @Parameterized.Parameters
    public static Collection<Object[]> instancesToTest() {
        objectMapper = new ObjectMapper();
        httpServletRequest = new MockHttpServletRequest();
        return Collections.singletonList(
                new Object[]{new ScheduleApiController(objectMapper, httpServletRequest)}
        );
    }
}