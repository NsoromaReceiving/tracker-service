package com.nsoroma.trackermonitoring.datasourceclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

    private  String trackerUrl;

    private String trackerStateUrl;

    private String hash;

    private String trackerString;

    private String trackerStateString;

    private Tracker trackerObject;

    private TrackerState trackerStateObject;

    private List<Tracker> trackerList;

    private List<TrackerState> trackerStateList;

    private List Ids;

    public TrackersClient() {}

    public Set<TrackerState> getTrackerState(String trackerIds, Set<Vehicle> vehiclesSet) {
        hash = authClient.getUserhash();
        trackerStateUrl = host + "tracker/get_states/?trackers="+trackerIds+"&hash=" + hash;
        System.out.println(trackerStateUrl);
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

    public Set<Tracker> getTrackers() {
        hash = authClient.getUserhash();
        trackerUrl = host + "tracker/list/?hash=" +hash;
        System.out.println(trackerUrl);
        trackerList = new ArrayList<Tracker>();

        try {
            HttpResponse<JsonNode> serverResponse = Unirest.get(trackerUrl).header("accept", "application/json")
                    .asJson();
            if (serverResponse.getStatus() == 200) {
                trackerString = serverResponse.getBody().getObject().getJSONArray("list").toString();
                ObjectMapper mapper = new ObjectMapper();
                trackerList = Arrays.asList(mapper.readValue(trackerString, Tracker[].class));
                System.out.println(trackerList);
            } else {
                System.out.println("Tracker fetch failed with response : ");
                System.out.println(serverResponse.getStatus());
                System.out.println(serverResponse.getBody());
            }
        } catch (UnirestException | IOException e) {
            System.out.println("Unable to make request to server " + e);
        }
        return new HashSet<Tracker>(trackerList);
    }
}