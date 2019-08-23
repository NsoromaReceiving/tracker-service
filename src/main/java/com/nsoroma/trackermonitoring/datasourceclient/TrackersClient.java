package com.nsoroma.trackermonitoring.datasourceclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.nsoroma.trackermonitoring.model.tracker.Tracker;
import com.nsoroma.trackermonitoring.model.trackerstate.TrackerState;
import com.nsoroma.trackermonitoring.model.vehicle.Vehicle;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TrackersClient {

    @Value("${nsoromagps.server2.host}")
    private String host;

    @Autowired
    Authentication authClient;

    private String trackerStateUrl;

    private String hash;

    private String trackerStateString;

    private TrackerState trackerStateObject;

    private List<TrackerState> trackerStateList;

    private List Ids;

    public TrackersClient() {}

    public Set<TrackerState> getTrackerState(String trackerIds, Set<Vehicle> vehiclesSet) {
        hash = authClient.getUserhash();
        trackerStateUrl = host + "tracker/get_states/?trackers="+trackerIds+"&hash=" + hash;
        System.out.println(trackerStateUrl);
        List<Tracker> List = Collections.emptyList();
        trackerStateList = new ArrayList<TrackerState>();
        Ids = new ArrayList<String>(Arrays.asList(trackerIds.substring(1, trackerIds.length()-1).split(",")));

        try {
            HttpResponse<JsonNode> serverResponse = Unirest.get(trackerStateUrl).header("accept", "application/json")
                    .asJson();
            if (serverResponse.getStatus() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                Iterator iterator = vehiclesSet.iterator();
                JSONObject states = serverResponse.getBody().getObject().getJSONObject("states");
                while(iterator.hasNext()){
                    Vehicle vehicle = (Vehicle) iterator.next();
                    if(vehicle.getTrackerId() != null) {
                        String trackerId = vehicle.getTrackerId().toString();
                        String label = vehicle.getLabel().toString();
                        String regNumber = vehicle.getRegNumber().toString();
                        trackerStateString = states.getJSONObject(trackerId).toString();
                        trackerStateObject = mapper.readValue(trackerStateString, TrackerState.class);
                        trackerStateObject.setTrackerId(trackerId);
                        trackerStateObject.setLabel(label);
                        trackerStateObject.setRegNumber(regNumber);
                        System.out.println(trackerStateObject);
                        trackerStateList.add(trackerStateObject);
                    }
                }
                
            } else {
                System.out.println("Vehicle fetch failed with response : ");
                System.out.println(serverResponse.getStatus());
                System.out.println(serverResponse.getBody());
            }
        } catch (UnirestException | IOException e) {
            System.out.println("Unable to make request to server " + e);
        }
        return new HashSet<TrackerState>(trackerStateList);
    }

}