package com.nsoroma.trackermonitoring.datasourceclient;

import java.util.List;
import com.nsoroma.trackermonitoring.datasourceclient.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.json.JSONObject;
import org.json.JSONArray;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;

@Service
public class Vehicles {

    @Value("${service.host}")
    private String host;

    @Autowired
    Authentication authClient;

    private String vehicleUrl;

    private String hash;

    private JSONArray vehicles;

    public Vehicles() { }

    public JSONArray getVehicles() {
        hash = authClient.getUserhash();
        vehicleUrl = host + "vehicle/list/?hash=" + hash;
        System.out.println(vehicleUrl);

        try {
            HttpResponse<JsonNode> serverResponse = Unirest.get(vehicleUrl)
            .header("accept", "application/json").asJson();
            if(serverResponse.getStatus() == 200) {
                vehicles = serverResponse.getBody().getObject().getJSONArray("list");
                System.out.println(vehicles);
            } else {
                System.out.println("Vehicle fetch failed with response : ");
                System.out.println(serverResponse.getStatus());
                System.out.println(serverResponse.getBody());
            }
        } catch (UnirestException e) {
            System.out.println("Unable to make request to server " + e);
        }
        return vehicles;
    }

}