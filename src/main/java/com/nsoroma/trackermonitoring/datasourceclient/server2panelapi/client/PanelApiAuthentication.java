package com.nsoroma.trackermonitoring.datasourceclient.server2panelapi.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;


@Service
public class PanelApiAuthentication {

    private Logger log = LoggerFactory.getLogger(PanelApiAuthentication.class);

    @Value("${nsoromagps.server2.panelAPI.host}")
    private String host;
    
    @Value("${nsoromagps.server2.panelAPI.username}")
    private String username;
    
	@Value("${nsoromagps.server2.panelAPI.password}")
    private String password;

    private String userHash;

    public String getDealerHash() throws UnirestException {
        String authenticationUrl = constructAuthenticationUrl();

        HttpResponse<JsonNode> serverResponse = Unirest.get(authenticationUrl)
        .header("accept", "application/json").asJson();
        if(serverResponse.getStatus() == 200) {
            userHash = serverResponse.getBody().getObject().getString("hash");
        } else {
            log.error("API authentication failed");
            String status =  String.valueOf(serverResponse.getStatus());
            log.debug(status);
        }
        return userHash;
    }

    public String constructAuthenticationUrl() {
        return host + "account/auth?login=" + username + "&password=" + password;
    }

    // for testing purposes
    public void setHost(String host) {
        this.host = host;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }



}