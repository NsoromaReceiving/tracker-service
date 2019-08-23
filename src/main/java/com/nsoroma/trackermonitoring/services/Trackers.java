package com.nsoroma.trackermonitoring.services;


import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

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
        return new HashSet<TrackerState>(trackerStateSet);
    }
}