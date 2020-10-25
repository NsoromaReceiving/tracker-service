package com.nsoroma.trackermonitoring.datasourceclient.server1api.client;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.nsoroma.trackermonitoring.datasourceclient.server1api.model.UserSession;
import com.nsoroma.trackermonitoring.exceptions.DataSourceClientResponseException;

import java.io.IOException;

public interface ApiAuthentication {

    public UserSession getUserSession() throws UnirestException, IOException, DataSourceClientResponseException;

}
