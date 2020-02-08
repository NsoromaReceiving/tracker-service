package com.nsoroma.trackermonitoring.restcontrollers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nsoroma.trackermonitoring.model.schedule.Schedule;
import com.nsoroma.trackermonitoring.scheduler.service.ScheduleClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.zone.ZoneRulesException;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class SchedulesApiTest {

    @InjectMocks
    SchedulesApi schedulesApi;

    private static ObjectMapper objectMapper;
    private static MockHttpServletRequest httpServletRequest;
    private ScheduleClient scheduleClient;

    public SchedulesApiTest (SchedulesApi schedulesApi) {
        this.schedulesApi = schedulesApi;
    }

    @Before
    public void setUp() throws Exception {
        scheduleClient = mock(ScheduleClient.class);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createNewScheduleWithAllTheCorrectParameters() {
        httpServletRequest.removeHeader("Accept");
        httpServletRequest.addHeader("Accept", "application/json");

        Schedule schedule = new Schedule();
        ZoneId zoneId = ZoneId.of("Europe/London");
        LocalDateTime localDateTime = LocalDateTime.now().plusDays(1);
        schedule.setZoneId(zoneId);
        schedule.setAlertTime(localDateTime);


        when(scheduleClient.createSchedule(schedule)).thenReturn(true);

        ResponseEntity<Void> responseEntity =  schedulesApi.newSchedule(schedule);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void getBadRequestIfAlertTimeIsBeforePresentTimeInNewSchedule() {
        httpServletRequest.removeHeader("Accept");
        httpServletRequest.addHeader("Accept", "application/json");

        Schedule schedule = new Schedule();
        ZoneId zoneId = ZoneId.of("Europe/London");
        LocalDateTime localDateTime = LocalDateTime.now().minusHours(3);
        schedule.setZoneId(zoneId);
        schedule.setAlertTime(localDateTime);

        when(scheduleClient.createSchedule(schedule)).thenReturn(true);

        ResponseEntity<Void> responseEntity =  schedulesApi.newSchedule(schedule);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void getInternalServerErrorWhenScheduleCreationReturnsFalse() {
        httpServletRequest.removeHeader("Accept");
        httpServletRequest.addHeader("Accept", "application/json");

        Schedule schedule = new Schedule();
        ZoneId zoneId = ZoneId.of("Europe/London");
        LocalDateTime localDateTime = LocalDateTime.now().plusDays(1);
        schedule.setZoneId(zoneId);
        schedule.setAlertTime(localDateTime);

        when(scheduleClient.createSchedule(schedule)).thenReturn(false);

        ResponseEntity<Void> responseEntity =  schedulesApi.newSchedule(schedule);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void getNotAcceptableStatusWhenAcceptHeaderIsNotPresentInNewSchedule() {
        httpServletRequest.removeHeader("Accept");

        Schedule schedule = new Schedule();

        ResponseEntity<Void> responseEntity =  schedulesApi.newSchedule(schedule);

        assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
    }


    @Test
    public void getInternalServerErrorWhenErrorOccursInScheduleCreation() throws ZoneRulesException {
        httpServletRequest.removeHeader("Accept");
        httpServletRequest.addHeader("Accept", "application/json");

        Schedule schedule = new Schedule();
        ZoneId zoneId = ZoneId.of("Europe/London");
        LocalDateTime localDateTime = LocalDateTime.now().plusDays(1);
        schedule.setZoneId(zoneId);
        schedule.setAlertTime(localDateTime);

        when(scheduleClient.createSchedule(schedule)).thenThrow(ZoneRulesException.class);

        ResponseEntity<Void> responseEntity =  schedulesApi.newSchedule(schedule);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void getAllSchedules() {
        httpServletRequest.removeHeader("Accept");
        httpServletRequest.addHeader("Accept", "application/json");

        Schedule schedule = new Schedule();
        List<Schedule> scheduleList = new ArrayList<>();
        when(scheduleClient.getScheduleList()).thenReturn(scheduleList);

        ResponseEntity<List<Schedule>> responseEntity =  schedulesApi.scheduleProfiles();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void getNotAcceptableWhenFormatIsNotApplicationJson() {
        httpServletRequest.removeHeader("Accept");
        httpServletRequest.addHeader("Accept", "xml");

        ResponseEntity<List<Schedule>> responseEntity =  schedulesApi.scheduleProfiles();

        assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
    }

    @Test
    public void getNotAcceptableWhenAcceptHeaderIsNotPresent() {
        httpServletRequest.removeHeader("Accept");

        ResponseEntity<List<Schedule>> responseEntity =  schedulesApi.scheduleProfiles();

        assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
    }

    @Test
    public void checkObjectMapper() {
        Optional<ObjectMapper> objectMapper = schedulesApi.getObjectMapper();
        assertTrue(objectMapper.isPresent());
    }



    @Parameterized.Parameters
    public static Collection<Object[]> instancesToTest() {
        objectMapper = new ObjectMapper();
        httpServletRequest = new MockHttpServletRequest();
        return Collections.singletonList(
                new Object[]{new SchedulesApiController(objectMapper, httpServletRequest)}
        );
    }
}