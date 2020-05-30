package com.nsoroma.trackermonitoring.datasourceclient.panelAPI.client;


import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import com.nsoroma.trackermonitoring.datasourceclient.panelAPI.model.Tracker;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Unirest.class})
public class PanelApiTrackerTest {

    @Mock
    private GetRequest getRequest;
    @Mock
    private HttpResponse<JsonNode> httpResponse;
    private PanelApiTrackerServiceImpl panelApiTrackerService;
    private String host = "testHost/";
    private String dealerHash = "dealerHash";

    @Before
    public void setUp() {
        PowerMockito.mockStatic(Unirest.class);
        panelApiTrackerService = new PanelApiTrackerServiceImpl();
        panelApiTrackerService.setHost(host);
    }


    @Test
    public void getAllTrackersWithoutAUserId() throws UnirestException, IOException {
        JsonNode json = new JsonNode("{\"list\":[{\"id\":123456789,\"label\":GV2131-19 ,\"dealer_id\":987654321," +
                " \"group_id\":123459876, \"user_id\":678912345, \"model_name\": testModelName," +
                " \"deleted\":false, \"creation_date\":someCreationDate, \"clone\":false, \"source\": " +
                "{\"id\":0192837465, \"device_id\":123456789, \"model\":someTrackerModel, \"phone\":somePhoneNo, " +
                " \"blocked\":false}}]}");

        when(httpResponse.getStatus()).thenReturn(200);
        when(httpResponse.getBody()).thenReturn(json);
        when(Unirest.get(host + "tracker/list/?hash=" + dealerHash)).thenReturn(getRequest);
        when(getRequest.header("accept", "application/json")).thenReturn(getRequest);
        when(getRequest.asJson()).thenReturn(httpResponse);

        List<Tracker> trackerList = panelApiTrackerService.getTrackerList(dealerHash, java.util.Optional.empty());
        assertThat(trackerList.size(), is(1));
        assertThat(trackerList, contains(hasProperty("id", is(123456789))));
    }

    @Test
    public void getATrackerWithId() throws UnirestException, IOException {
        String trackerId = "testTrackerId";
        JsonNode json = new JsonNode("{\"value\":{\"id\":123456789,\"label\":GV2131-19 ,\"dealer_id\":987654321," +
                " \"group_id\":123459876, \"user_id\":678912345, \"model_name\": testModelName," +
                " \"deleted\":false, \"creation_date\":someCreationDate, \"clone\":false, \"source\": " +
                "{\"id\":0192837465, \"device_id\":123456789, \"model\":someTrackerModel, \"phone\":somePhoneNo}}}");

        when(httpResponse.getStatus()).thenReturn(200);
        when(httpResponse.getBody()).thenReturn(json);
        when(Unirest.get(host + "tracker/read/?tracker_id="+ trackerId + "&hash=" + dealerHash)).thenReturn(getRequest);
        when(getRequest.header("accept", "application/json")).thenReturn(getRequest);
        when(getRequest.asJson()).thenReturn(httpResponse);

        Tracker tracker = panelApiTrackerService.getTracker(dealerHash, trackerId);
        assertThat(tracker, hasProperty("id", is(123456789)));

    }

    @Test
    public void checkConstructTrackerUrl() {
        String trackerid = "trackerId";
        String trackerUrl = panelApiTrackerService.constructTrackerUrl(dealerHash,trackerid);
        String expected = host + "tracker/read/?tracker_id=" + trackerid + "&hash=" + dealerHash;
        assertEquals("wrong tracker url in panel api tracker service", expected, trackerUrl);
    }

    @Test
    public void checkConstructTrackersUrlWithUserId() {
        String userId = "testUserId";
        String trackersUrl = panelApiTrackerService.constructTrackersURL(dealerHash, java.util.Optional.of(userId));
        String expected = host + "tracker/active/list/?user_id=" + userId + "&hash=" + dealerHash;
        assertEquals("wrong tracker url in panel api tracker service", expected, trackersUrl);
    }

    @Test
    public void checkConstructTrackersUrlWithoutUserId() {
        String trackerUrl = panelApiTrackerService.constructTrackersURL(dealerHash, java.util.Optional.empty());
        String expected = host + "tracker/list/?hash=" + dealerHash;
        assertEquals("wrong trackers url in trackers panel api service", expected, trackerUrl);
    }

    @Test
    public void returnNullTrackerObjectWhenStatusIsNot200() throws UnirestException, IOException {

        String trackerId = "testTrackerId";

        when(httpResponse.getStatus()).thenReturn(400);
        when(Unirest.get(host + "tracker/read/?tracker_id="+ trackerId + "&hash=" + dealerHash)).thenReturn(getRequest);
        when(getRequest.header("accept", "application/json")).thenReturn(getRequest);
        when(getRequest.asJson()).thenReturn(httpResponse);

        Tracker tracker = panelApiTrackerService.getTracker(dealerHash, trackerId);
        verify(httpResponse, never()).getBody();
    }
}
