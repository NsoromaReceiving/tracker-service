package com.nsoroma.trackermonitoring.datasourceclient.panelAPI.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.json.JSONObject;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;


@Service
public class PanelApiAuthentication {
    @Value("${nsoromagps.server2.panelAPI.host}")
    private String host;
    
    @Value("${nsoromagps.server2.panelAPI.username}")
    private String username;
    
	@Value("${nsoromagps.server2.panelAPI.password}")
    private String password;

    private String authenticationUrl;

    private String userHash;

    public PanelApiAuthentication() { }

    public String getDealerHash() {
        authenticationUrl = host + "account/auth?login=" + username + "&password=" + password;
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