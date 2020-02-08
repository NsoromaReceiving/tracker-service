package com.nsoroma.trackermonitoring.datasourceclient.panelAPI.client;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nsoroma.trackermonitoring.datasourceclient.panelAPI.model.Tracker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class PanelApiTrackerServiceImpl implements PanelApiTrackerService {

    @Value("${nsoromagps.server2.panelAPI.host}")
    private
    String host;

    @Override
    public List<Tracker> getTrackerList(String hash, Optional<String> userId) throws IOException, UnirestException {
        String trackerURL = "";
        trackerURL = constructTrackersURL(hash, userId);
        List<Tracker> trackerList = new ArrayList<>();

        HttpResponse<JsonNode> trackerResponse = Unirest.get(trackerURL).header("accept", "application/json").asJson();
        if(trackerResponse.getStatus() == 200) {
            ObjectMapper objectMapper = new ObjectMapper();
            String trackerObjectString = trackerResponse.getBody().getObject().getJSONArray("list").toString();
            trackerList = Arrays.asList(objectMapper.readValue(trackerObjectString, Tracker[].class));
        }

        return trackerList;
    }

    @Override
    public Tracker getTracker(String hash, String trackerId) throws UnirestException, IOException {
        String trackerURL = constructTrackerUrl(hash, trackerId);
        Tracker tracker = new Tracker();

        HttpResponse<JsonNode> trackerResponse = Unirest.get(trackerURL).header("accept", "application/json").asJson();
        if(trackerResponse.getStatus() == 200) {
            ObjectMapper objectMapper = new ObjectMapper();
            String trackerObjectString = trackerResponse.getBody().getObject().getJSONObject("value").toString();
            tracker = objectMapper.readValue(trackerObjectString, Tracker.class);
        }

        return tracker;
    }

    protected String constructTrackerUrl(String hash, String trackerId) {
        return host + "tracker/read/?tracker_id=" + trackerId + "&hash=" + hash;
    }

    protected String constructTrackersURL(String hash, Optional<String> userId) {
        return userId.map(s -> host + "tracker/list/?user_id=" + s + "&hash=" + hash).orElseGet(() -> host + "tracker/list/?hash=" + hash);
    }

    protected void setHost(String host) { this.host = host; }
}
