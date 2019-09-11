package com.nsoroma.trackermonitoring.datasourceclient.api.client;

import com.nsoroma.trackermonitoring.datasourceclient.api.model.TrackerLastState;

import java.io.IOException;
import java.util.List;

public interface ApiTrackerService {

    List<TrackerLastState> getTrackerLastState(String hash, List<String> trackerIdList) throws IOException;

}
