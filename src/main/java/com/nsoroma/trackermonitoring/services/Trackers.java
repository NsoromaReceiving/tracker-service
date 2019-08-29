package com.nsoroma.trackermonitoring.services;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public LinkedHashSet<TrackerState> getTrackers(Optional<String> duration, Optional<String> customerId, Optional<String> type, Optional<String> order) {

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
        trackerStateSet = setTrackerInfo(trackerStateSet, trackerSet);
        trackerStateSet = filterTrackers(duration, customerId, type, order , trackerStateSet);
        return new LinkedHashSet<>(trackerStateSet);
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

    public LinkedHashSet<TrackerState> filterTrackers(Optional<String> duration, Optional<String> customerId, Optional<String> type, Optional<String> order, Set<TrackerState>trackerStates) {
        order.orElse("dsc");
        //System.out.println("duration= " + duration.get() + " customerID= " + customerId.get() + " type= " + type.get() + "order= " + order.get() );

        if (duration.isPresent()) {
            System.out.println("Duration= " + duration.get());
            trackerStates = trackerStates.stream().filter(trackerState -> trackerState.getLastUpdate().substring(0,10).equals(duration.get())).collect(Collectors.toSet());
        }
        if (customerId.isPresent()) {
            System.out.println("customerID present");
        }
        if (type.isPresent()){
            System.out.println("Type= " + type.get());
            trackerStates = trackerStates.stream().filter(trackerState -> trackerState.getTrackerType().equals(type.get())).collect(Collectors.toSet());
        }
        if (order.isPresent()) {
            System.out.println(order.get());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            trackerStates = trackerStates.stream().sorted(Comparator.comparing(TrackerState::getLastUpdate, (date1, date2) -> {
                try {
                    Date d1 = sdf.parse(date1);
                    Date d2 = sdf.parse(date2);
                    if(order.get().equals("dsc")) {
                        return (d1.getTime() > d2.getTime() ? -1 : 1); //descending
                    } else {
                        return (d1.getTime() > d2.getTime() ? 1 : -1); //ascending
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return date2.compareTo(date1);
            })).collect(Collectors.toCollection(LinkedHashSet::new));
        }

        System.out.println(trackerStates);
        return new LinkedHashSet<>(trackerStates);
    }
}