package com.nsoroma.trackermonitoring.datasourceclient.server2api.client;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import com.nsoroma.trackermonitoring.datasourceclient.server2api.model.TrackerLastState;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.hamcrest.CoreMatchers.is;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Unirest.class)
public class ApiTrackerServiceImplTest {

    @Mock
    GetRequest getRequest;
    @Mock
    HttpResponse<JsonNode> httpResponse;
    private ApiTrackerServiceImpl apiTrackerService;
    private String host = "testHost/";
    private String customerHash = "customerHash";

    @Before
    public void setUp() {
        PowerMockito.mockStatic(Unirest.class);
        apiTrackerService = new ApiTrackerServiceImpl();
        apiTrackerService.setHost(host);
    }

    @Test
    public void getTrackerLastState() throws UnirestException, IOException {
        JsonNode jsonNode = new JsonNode("{\"states\":{\"343978\":{\"source_id\":233264 ,\"gps\":{\"updated\": lastUpdate," +
                "\"signal_level\": 100, location: {\"lat\": 5.572348723, \"lng\": -0202356733}, \"heading\": 80," +
                "\"speed\":0, \"alt\":29}, \"connection_status\": idle, \"movement_status\": stopped, \"gsm\":{" +
                "\"updated\": gsmUpdated, \"signal_level\":83, \"network_name\":MTN, \"roaming\": null}, \"last_update\":" +
                "lastUpdateDate, \"battery_level\":100}}}");

        List<String> trackerIds = Collections.singletonList("343978");
        when(httpResponse.getStatus()).thenReturn(200);
        when(httpResponse.getBody()).thenReturn(jsonNode);
        when(Unirest.get(host + "tracker/get_states/?trackers=" + "[343978]"+ "&hash=" + customerHash)).thenReturn(getRequest);
        when(getRequest.header("accept", "application/json")).thenReturn(getRequest);
        when(getRequest.asJson()).thenReturn(httpResponse);

        List<TrackerLastState> trackerLastStateList = apiTrackerService.getTrackerLastState(customerHash,trackerIds);
        assertThat(trackerLastStateList.size(), is(1));
        assertThat(trackerLastStateList, contains(hasProperty("trackerId", is("343978"))));
    }


    @Test
    public void returnEmptyListIfNot200() throws UnirestException, IOException {

        List<String> trackerIds = Collections.singletonList("343978");

        when(httpResponse.getStatus()).thenReturn(404);
        when(Unirest.get(host + "tracker/get_states/?trackers=" + "[343978]"+ "&hash=" + customerHash)).thenReturn(getRequest);
        when(getRequest.header("accept", "application/json")).thenReturn(getRequest);
        when(getRequest.asJson()).thenReturn(httpResponse);

        List<TrackerLastState> trackerLastStateList = apiTrackerService.getTrackerLastState(customerHash,trackerIds);
        assertThat(trackerLastStateList.size(), is(0));
    }


    @Test
    public void checkConstructTrackerStatesUrl() {
        String trackerIds = "trackerIds";
        String trackerStateUrl = apiTrackerService.constructTrackerStatesUrl(customerHash, trackerIds);
        String expected = host + "tracker/get_states/?trackers=" + trackerIds + "&hash=" + customerHash;
        assertEquals("wrong tracker state url constructed", expected, trackerStateUrl);
    }

    @Test
    public void checkTrackerIdStringConstruction() {
        List<String> trackerIds = Arrays.asList("1234567", "8901234", "5678901");
        String trackerIdsStr = apiTrackerService.getTrackerIdsString(trackerIds);
        String expected = "[1234567,8901234,5678901]";
        assertEquals("tracker ID strings not equal at apiTrackerService", expected, trackerIdsStr);
    }
}