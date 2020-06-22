package com.nsoroma.trackermonitoring.serviceutils;

import com.nsoroma.trackermonitoring.datasourceclient.api.model.TrackerLastState;
import com.nsoroma.trackermonitoring.model.trackerstate.TrackerState;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TrackerStateUtilsImpl implements TrackerStateUtils {

    public void checkAndSetGpsData(TrackerState trackerState, TrackerLastState trackerLastState) {
        if (trackerLastState.getGps().getUpdated() != null) {
            trackerState.setLastGpsUpdate(trackerLastState.getGps().getUpdated());
        }
        if (trackerLastState.getGps().getSignalLevel() != null) {
            trackerState.setLastGpsSignalLevel(trackerLastState.getGps().getSignalLevel().toString());
        }
        if (trackerLastState.getGps().getLocation().getLat() != null) {
            trackerState.setLastGpsLatitude(trackerLastState.getGps().getLocation().getLat().toString());
        }
        if (trackerLastState.getGps().getLocation().getLng() != null) {
            trackerState.setLastGpsLongitude(trackerLastState.getGps().getLocation().getLng().toString());
        }
    }

    @Override
    public void checkAndSetGsmData(TrackerState trackerState, TrackerLastState trackerLastState) {
        if (trackerLastState.getGsm().getSignalLevel() != null) {
            trackerState.setGsmSignalLevel(trackerLastState.getGsm().getSignalLevel().toString());
        }
        if (trackerLastState.getGsm().getNetworkName() != null) {
            trackerState.setGsmNetworkName(trackerLastState.getGsm().getNetworkName());
        }
        if (trackerLastState.getGsm().getUpdated() != null) {
            trackerState.setLastGsmUpdate(trackerLastState.getGsm().getUpdated());
        }
    }

    @Override
    public Set<TrackerState> sortTrackerStatesByOrder(Optional<String> order, Set<TrackerState> trackerStates, String datePattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
        trackerStates = trackerStates.stream().sorted(Comparator.comparing(TrackerState::getLastGsmUpdate, (date1, date2) -> {
            try {
                if(date1 != null && date2 != null) {
                    Date d1 = sdf.parse(date1);
                    Date d2 = sdf.parse(date2);
                    if(order.isPresent() && order.get().equals("dsc")) {
                        return (d1.getTime() > d2.getTime() ? -1 : 1); //descending
                    } else {
                        return (d1.getTime() > d2.getTime() ? 1 : -1); //ascending
                    }
                }
            } catch (ParseException e) {
                return 0;
            }
            return 0;
        })).collect(Collectors.toCollection(LinkedHashSet::new));
        return trackerStates;
    }

    @Override
    public Set<TrackerState> trimTrackerStatesByStartDate(Optional<String> startDate, Set<TrackerState> trackerStates, String datePattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
        trackerStates = trackerStates.stream().filter(trackerState -> {
            try {
                return trackerState.getLastGsmUpdate() != null &&
                        sdf.parse(trackerState.getLastGsmUpdate()).after(sdf.parse(startDate.get()));
            } catch (ParseException e) {
                return false;
            }

        }).collect(Collectors.toSet());
        return trackerStates;
    }

    @Override
    public Set<TrackerState> trimTrackerStatesByEndDate(Optional<String> endDate, Set<TrackerState> trackerStates, String datePattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
        trackerStates = trackerStates.stream().filter(trackerState -> {
            try {
                return trackerState.getLastGsmUpdate() != null &&
                        sdf.parse(trackerState.getLastGsmUpdate()).before(sdf.parse(endDate.get()));
            } catch (ParseException e) {
                return false;
            }
        }).collect(Collectors.toSet());
        return trackerStates;
    }
}
