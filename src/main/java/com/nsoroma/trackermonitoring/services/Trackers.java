package com.nsoroma.trackermonitoring.services;


import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.nsoroma.trackermonitoring.datasourceclient.api.client.ApiTrackerService;
import com.nsoroma.trackermonitoring.datasourceclient.api.model.TrackerLastState;
import com.nsoroma.trackermonitoring.datasourceclient.panelAPI.client.PanelApiAuthentication;
import com.nsoroma.trackermonitoring.datasourceclient.panelAPI.client.PanelApiCustomerService;
import com.nsoroma.trackermonitoring.datasourceclient.panelAPI.client.PanelApiTrackerService;
import com.nsoroma.trackermonitoring.datasourceclient.panelAPI.model.Customer;
import com.nsoroma.trackermonitoring.datasourceclient.panelAPI.model.Tracker;
import com.nsoroma.trackermonitoring.model.trackerstate.TrackerState;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class Trackers {

    @Autowired
    PanelApiAuthentication dealerAuthClient;

    @Autowired
    PanelApiCustomerService customerService;

    @Autowired
    PanelApiTrackerService panelTrackersService;

    @Autowired
    ApiTrackerService apiTrackerService;

    public Trackers(){}

    public LinkedHashSet<TrackerState> getTrackers(Optional<String> duration, Optional<String> customerId, Optional<String> type, Optional<String> order) throws IOException {

        List<TrackerState> trackerStates = new ArrayList<TrackerState>();

        List<Tracker> trackerList = getTrackerList();
        List<Customer> customerList = getCustomerList();
        List<TrackerLastState> trackerLastStateList = new ArrayList<TrackerLastState>();
        for(Customer customer: customerList) {
            List<TrackerLastState> customerTrackerLastStateList = getTrackerStateList(customer.getId().toString(),trackerList);
            if(customerTrackerLastStateList != null) {
                trackerLastStateList.addAll(customerTrackerLastStateList);
            }
        }
        for(Tracker tracker: trackerList) {
            TrackerState trackerState = new TrackerState();
            trackerState.setLabel(tracker.getLabel());
            trackerState.setCustomerId(tracker.getUserId().toString());
            trackerState.setTrackerId(tracker.getId().toString());
            trackerState.setImei(tracker.getSource().getDeviceId());
            trackerState.setModel(tracker.getSource().getModel());
            trackerState.setPhoneNumber(tracker.getSource().getPhone());
            trackerState.setConnectionStatus(tracker.getSource().getConnectionStatus());
            trackerState.setTariffEndDate(tracker.getSource().getTariffEndDate());
            List<Customer> trackerCustomer = customerList.stream().filter(customer -> customer.getId().toString().equals(trackerState.getCustomerId())).collect(Collectors.toList());
            if(trackerCustomer.size() > 0){
                String customerName = trackerCustomer.get(0).getFirstName() +" "+ trackerCustomer.get(0).getMiddleName() + " " + trackerCustomer.get(0).getLastName();
                trackerState.setCustomerName(customerName);
            }
            List<TrackerLastState> lastState = trackerLastStateList.stream().filter(trackerLastState -> trackerLastState.getTrackerId().equals(trackerState.getTrackerId())).collect(Collectors.toList());
            if(lastState.size() > 0) {
                System.out.println(lastState.get(0));
                if(lastState.get(0).getGps() != null) {
                    if(lastState.get(0).getGps().getUpdated() != null) {
                        trackerState.setLastGpsUpdate(lastState.get(0).getGps().getUpdated());
                    }
                    if(lastState.get(0).getGps().getSignalLevel() != null){
                        trackerState.setLastGpsSignalLevel(lastState.get(0).getGps().getSignalLevel().toString());
                    }
                    if(lastState.get(0).getGps().getLocation().getLat() != null) {
                        trackerState.setLastGpsLatitude(lastState.get(0).getGps().getLocation().getLat().toString());
                    }
                    if(lastState.get(0).getGps().getLocation().getLng() != null) {
                        trackerState.setLastGpsLongitude(lastState.get(0).getGps().getLocation().getLng().toString());
                    }
                }
                if(lastState.get(0).getGsm() != null) {
                    if(lastState.get(0).getGsm().getSignalLevel() != null) {
                        trackerState.setGsmSignalLevel(lastState.get(0).getGsm().getSignalLevel().toString());
                    }
                    if(lastState.get(0).getGsm().getNetworkName() != null){
                        trackerState.setGsmNetworkName(lastState.get(0).getGsm().getNetworkName());
                    }
                }
                if(lastState.get(0).getBatteryLevel() != null) {
                    trackerState.setLastBatteryLevel(lastState.get(0).getBatteryLevel().toString());
                }

            }

            trackerStates.add(trackerState);
        }
        return new LinkedHashSet<>(trackerStates);
    }



    /* public LinkedHashSet<TrackerState> filterTrackers(Optional<String> duration, Optional<String> customerId, Optional<String> type, Optional<String> order, Set<TrackerState>trackerStates) {
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
    }*/

    public List<Tracker> getTrackerList() throws IOException {
        String hash = dealerAuthClient.getDealerHash();
        return panelTrackersService.getTrackerList(hash);
    }

    public List<Customer> getCustomerList() throws IOException {
        String hash = dealerAuthClient.getDealerHash();
        return customerService.getCustomers(hash);
    }
    public List<TrackerLastState> getTrackerStateList(String customerId, List<Tracker> trackers) throws IOException {
        String hash = dealerAuthClient.getDealerHash();
        String customerHash = customerService.getCustomerHash(hash, customerId);
        List<String> trackerIds = getCustomerTrackerIdList(trackers, customerId);
        if(trackerIds.size() > 0) {
            return apiTrackerService.getTrackerLastState(customerHash, trackerIds);
        }
        return null;
    }




    public List<String> getCustomerTrackerIdList(List<Tracker> trackers, String customerId) {

        List<Tracker> customerTrackers = trackers.stream().filter(tracker -> tracker.getUserId().toString().equals(customerId)).collect(Collectors.toList());
        List<String> customerTrackerIds = new ArrayList<String>();
        for(Tracker customerTracker: customerTrackers) {
            customerTrackerIds.add(customerTracker.getId().toString());
        }
        return customerTrackerIds;
    }

}