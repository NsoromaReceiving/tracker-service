package com.nsoroma.trackermonitoring.datasourceclient.panelAPI.client;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.nsoroma.trackermonitoring.datasourceclient.panelAPI.model.Tracker;

import java.io.IOException;
import java.util.List;

public interface TrackerService {

     List<Tracker> getTrackerList(String hash) throws JsonMappingException, IOException;

}
