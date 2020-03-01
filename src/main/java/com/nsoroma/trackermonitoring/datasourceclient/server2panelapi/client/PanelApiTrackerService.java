package com.nsoroma.trackermonitoring.datasourceclient.server2panelapi.client;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.nsoroma.trackermonitoring.datasourceclient.server2panelapi.model.Tracker;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface PanelApiTrackerService {

     List<Tracker> getTrackerList(String hash, Optional<String> userId) throws IOException, UnirestException;

     Tracker getTracker(String hash, String TrackerId) throws IOException, UnirestException;

}
