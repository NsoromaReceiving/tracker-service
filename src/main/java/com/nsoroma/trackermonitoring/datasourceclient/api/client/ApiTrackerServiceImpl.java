package com.nsoroma.trackermonitoring.datasourceclient.api.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.nsoroma.trackermonitoring.datasourceclient.api.model.TrackerLastState;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ApiTrackerServiceImpl implements ApiTrackerService {

    @Value("${nsoromagps.server2.api.host}")
    String host;


    @Override
    public List<TrackerLastState> getTrackerLastState(String hash, List<String> trackerIdList) throws IOException {

        String trackerIds = new String();
        for(String trackerId: trackerIdList){
            trackerIds += trackerId +",";
        }
        System.out.println(trackerIds);
        trackerIds = "[" + trackerIds.substring(0, trackerIds.length()-1) + "]";
        String trackerStateUrl = host + "tracker/get_states/?trackers=" + trackerIds + "&hash=" + hash;
        System.out.println(trackerStateUrl);
        List<TrackerLastState> trackerLastStates = new ArrayList<TrackerLastState>();

        try {
            HttpResponse<JsonNode> trackerStateResponse = Unirest.get(trackerStateUrl).header("accept","application/json").asJson();
            System.out.println(trackerStateResponse.getStatus());
            if(trackerStateResponse.getStatus() == 200) {
                ObjectMapper objectMapper = new ObjectMapper();
                JSONObject trackerStatesObject = trackerStateResponse.getBody().getObject().getJSONObject("states");
                System.out.println(trackerStatesObject);
                for (String trackerId: trackerIdList) {
                    String trackerIdStateString = trackerStatesObject.getJSONObject(trackerId.toString()).toString();
                    TrackerLastState trackerLastState = objectMapper.readValue(trackerIdStateString,TrackerLastState.class);
                    trackerLastState.setTrackerId(trackerId);
                    trackerLastStates.add(trackerLastState);
                }
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        System.out.println(trackerLastStates);
        return trackerLastStates;
    }
}
