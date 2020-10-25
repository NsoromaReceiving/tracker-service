package com.nsoroma.trackermonitoring.datasourceclient.server1api.client;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.nsoroma.trackermonitoring.datasourceclient.server1api.model.LatestLocation;
import com.nsoroma.trackermonitoring.exceptions.DataSourceClientResponseException;

import java.io.IOException;
import java.util.List;

public interface LocationManager {

    public List<LatestLocation> getLatestLocation() throws UnirestException, IOException, DataSourceClientResponseException;
}
