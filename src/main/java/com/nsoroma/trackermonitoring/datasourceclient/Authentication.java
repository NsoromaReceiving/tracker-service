package com.nsoroma.trackermonitoring.datasourceclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.json.JSONObject;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;


@Service
public class Authentication {
    @Value("${nsoromagps.server2.host}")
    private String host;
    
    @Value("${nsoromagps.server2.username}")
    private String username;
    
	@Value("${nsoromagps.server2.password}")
    private String password;

    private String authenticationUrl;

    private String userHash;

    public Authentication() { }

    public String getUserhash() {
        authenticationUrl = host + "user/auth?login=" + username + "&password=" + password;
        System.out.println(authenticationUrl);
        try {
            HttpResponse<JsonNode> serverResponse = Unirest.get(authenticationUrl)
            .header("accept", "application/json").asJson();
            if(serverResponse.getStatus() == 200) {
                userHash = serverResponse.getBody().getObject().getString("hash");
            } else {
                System.out.println("User authentication failed");
                System.out.println(serverResponse.getStatus());
            }
        } catch (UnirestException e) {
            System.out.println("Unable to make request to server " + e);
        }
        return userHash;
    }


 }