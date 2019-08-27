package com.nsoroma.trackermonitoring.services;


import java.util.*;

import com.nsoroma.trackermonitoring.datasourceclient.Vehicles;
import com.nsoroma.trackermonitoring.model.tracker.Tracker;
import com.nsoroma.trackermonitoring.model.trackerstate.TrackerState;
import com.nsoroma.trackermonitoring.model.vehicle.Vehicle;
import com.nsoroma.trackermonitoring.datasourceclient.TrackersClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class Trackers {

    @Autowired
    Vehicles vehicleClient;

    @Autowired
    TrackersClient trackersClient;

    private Set<Vehicle> vehicleSet;

    private String vehicleTrackerIds;

    private Set<TrackerState> trackerStateSet;

    private Set<Tracker> trackerSet;

    public Trackers(){}

    public Set<TrackerState> getTrackers() {
        vehicleSet = vehicleClient.getVehicles();
        
        Iterator iterator = vehicleSet.iterator();

        String ids = "";
        String vehicleLabel;
        String regNumber;

        while(iterator.hasNext()){
            Vehicle vehicle = (Vehicle) iterator.next();
            if(vehicle.getTrackerId() != null){
                ids += vehicle.getTrackerId().toString() + ",";
            }
        }

        vehicleTrackerIds = "[" + ids.substring(0, ids.length()-1) + "]";

        trackerStateSet = trackersClient.getTrackerState(vehicleTrackerIds, vehicleSet);
        trackerSet = trackersClient.getTrackers();
        return new HashSet<TrackerState>(setTrackerInfo(trackerStateSet, trackerSet));
    }

    public Set<TrackerState> setTrackerInfo(Set<TrackerState> trackerStates, Set<Tracker> trackers) {
        Iterator iterator = trackerStates.iterator();
        List<Tracker> trackerList = new ArrayList<Tracker>(trackers);
        while(iterator.hasNext()){
            TrackerState trackerState = (TrackerState) iterator.next();
            if(trackerState.getTrackerId() != null) {
                String trackerId = trackerState.getTrackerId();
                for (int i = 0; i < trackerList.size(); i++) {
                    String Id = trackerList.get(i).getId().toString();
                    if(trackerId.equals(Id)) {
                        trackerState.setTrackerType(trackerList.get(i).getSource().getModel());
                        trackerState.setTrackerImei(trackerList.get(i).getSource().getDeviceId());
                    }
                }

            }
        }
        return new HashSet<TrackerState>(trackerStates);
    }
}