package com.nsoroma.trackermonitoring.datasourceclient.server2api.client;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.nsoroma.trackermonitoring.datasourceclient.server2api.model.TrackerLastState;

import java.io.IOException;
import java.util.List;

public interface ApiTrackerService {

    List<TrackerLastState> getTrackerLastState(String hash, List<String> trackerIdList) throws IOException, UnirestException;

}
