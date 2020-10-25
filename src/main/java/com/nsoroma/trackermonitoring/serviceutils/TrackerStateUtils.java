package com.nsoroma.trackermonitoring.serviceutils;

import com.nsoroma.trackermonitoring.datasourceclient.server2api.model.TrackerLastState;
import com.nsoroma.trackermonitoring.model.trackerstate.TrackerState;

import java.util.Optional;
import java.util.Set;

public interface TrackerStateUtils {

    public void checkAndSetGpsData(TrackerState trackerState, TrackerLastState trackerLastState);

    public void checkAndSetGsmData(TrackerState trackerState, TrackerLastState trackerLastState);

    public Set<TrackerState> sortTrackerStatesByOrder(Optional<String> order, Set<TrackerState> trackerStates, String datePattern);

    public Set<TrackerState> trimTrackerStatesByStartDate(Optional<String> startDate, Set<TrackerState> trackerStates, String datePattern);

    public Set<TrackerState> trimTrackerStatesByEndDate(Optional<String> endDate, Set<TrackerState> trackerStates, String datePattern);
}
