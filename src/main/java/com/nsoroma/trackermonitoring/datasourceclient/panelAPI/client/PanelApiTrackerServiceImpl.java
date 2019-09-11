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

@Service
public class PanelApiTrackerServiceImpl implements PanelApiTrackerService {

    @Value("${nsoromagps.server2.panelAPI.host}")
    String host;

    @Override
    public List<Tracker> getTrackerList(String hash) throws IOException {
        String trackerUrl = host + "tracker/list/?hash=" + hash;
        List<Tracker> trackerList = new ArrayList<Tracker>();

    try{
        HttpResponse<JsonNode> trackerResponse = Unirest.get(trackerUrl).header("accept", "application/json").asJson();
        if(trackerResponse.getStatus() == 200) {
            ObjectMapper objectMapper = new ObjectMapper();
            String trackerObjectString = trackerResponse.getBody().getObject().getJSONArray("list").toString();
            trackerList = Arrays.asList(objectMapper.readValue(trackerObjectString, Tracker[].class));
        }
    } catch (UnirestException e) {
        e.printStackTrace();
    }

        return trackerList;
    }
}
