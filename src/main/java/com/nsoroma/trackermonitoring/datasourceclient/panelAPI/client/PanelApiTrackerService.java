package com.nsoroma.trackermonitoring.datasourceclient.panelAPI.client;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.nsoroma.trackermonitoring.datasourceclient.panelAPI.model.Tracker;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface PanelApiTrackerService {

     List<Tracker> getTrackerList(String hash, Optional<String> userId) throws JsonMappingException, IOException;

     Tracker getTracker(String hash, String TrackerId) throws IOException, UnirestException;

}
