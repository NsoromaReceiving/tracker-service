package com.nsoroma.trackermonitoring.datasourceclient;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import com.nsoroma.trackermonitoring.datasourceclient.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.json.JSONObject;
import org.json.JSONArray;
import com.mashape.unirest.http.Unirest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.nsoroma.trackermonitoring.model.vehicle.Vehicle;

@Service
public class Vehicles {

    @Value("${nsoromagps.server2.host}")
    private String host;

    @Autowired
    Authentication authClient;

    private String vehicleUrl;

    private String hash;

    private String vehicles;

    public Vehicles() {}

    public Set<Vehicle> getVehicles() {
        hash = authClient.getUserhash();
        vehicleUrl = host + "vehicle/list/?hash=" + hash;
        System.out.println(vehicleUrl);
        List<Vehicle> vehicleList = Collections.emptyList();
        try {
            HttpResponse<JsonNode> serverResponse = Unirest.get(vehicleUrl).header("accept", "application/json")
                    .asJson();
            if (serverResponse.getStatus() == 200) {
                vehicles = serverResponse.getBody().getObject().getJSONArray("list").toString();
                ObjectMapper mapper = new ObjectMapper();
                vehicleList = Arrays.asList(mapper.readValue(vehicles, Vehicle[].class));
                System.out.println(vehicleList);
            } else {
                System.out.println("Vehicle fetch failed with response : ");
                System.out.println(serverResponse.getStatus());
                System.out.println(serverResponse.getBody());
            }
        } catch (UnirestException | IOException e) {
            System.out.println("Unable to make request to server " + e);
        }
        return new HashSet<Vehicle>(vehicleList);
    }

}