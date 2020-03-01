package com.nsoroma.trackermonitoring.datasourceclient.api.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private Logger log = LoggerFactory.getLogger(ApiTrackerServiceImpl.class);

    @Value("${nsoromagps.server2.api.host}")
    private
    String host;


    @Override
    public List<TrackerLastState> getTrackerLastState(String hash, List<String> trackerIdList) throws IOException, UnirestException {

        String trackerIds = getTrackerIdsString(trackerIdList);

        String trackerStateUrl = constructTrackerStatesUrl(hash, trackerIds);
        log.info(trackerStateUrl);
        List<TrackerLastState> trackerLastStates = new ArrayList<>();

        HttpResponse<JsonNode> trackerStateResponse = Unirest.get(trackerStateUrl).header("accept","application/json").asJson();
        if(trackerStateResponse.getStatus() == 200) {
            log.info("Unirest Successful");
            ObjectMapper objectMapper = new ObjectMapper();
            JSONObject trackerStatesObject = trackerStateResponse.getBody().getObject().getJSONObject("states");
            for (String trackerId: trackerIdList) {
                String trackerIdStateString = trackerStatesObject.getJSONObject(trackerId).toString();
                TrackerLastState trackerLastState = objectMapper.readValue(trackerIdStateString,TrackerLastState.class);
                trackerLastState.setTrackerId(trackerId);
                trackerLastStates.add(trackerLastState);
            }
        }
        return trackerLastStates;
    }

    protected String constructTrackerStatesUrl(String hash, String trackerIds) {
        return host + "tracker/get_states/?trackers=" + trackerIds + "&hash=" + hash;
    }

    protected String getTrackerIdsString(List<String> trackerIdList) {
        StringBuilder trackerIds = new StringBuilder();
        for(String trackerId: trackerIdList){
            trackerIds.append(trackerId).append(",");
        }
        String ids = String.valueOf(trackerIds);
        log.info(ids);
        trackerIds = new StringBuilder("[" + trackerIds.substring(0, trackerIds.length() - 1) + "]");
        return trackerIds.toString();
    }


    protected void setHost(String host) {
        this.host = host;
    }
}
