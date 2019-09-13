package com.nsoroma.trackermonitoring.services;


import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.mashape.unirest.http.exceptions.UnirestException;
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

    //serves api/trackers/?param1=&param2=...
    public LinkedHashSet<TrackerState> getTrackers(Optional<String> duration, Optional<String> customerId, Optional<String> type, Optional<String> order) throws IOException {

        String hash = dealerAuthClient.getDealerHash();
        List<Tracker> trackerList = getTrackerList(customerId, hash); //gets list of all trackers on server 2 which may belong to a user
        List<Customer> customerList;
        Set<TrackerState> trackerStates = new HashSet<>(Collections.emptySet());
        List<TrackerLastState> trackerLastStateList = new ArrayList<TrackerLastState>();

        if(customerId.isPresent()){
            List<TrackerLastState> customerTrackerLastStateList = getTrackerStateList(customerId.get(),trackerList,hash);
            System.out.println(customerTrackerLastStateList);
            if(customerTrackerLastStateList != null) {
                System.out.println("came here");
                trackerLastStateList.addAll(customerTrackerLastStateList);
            }
        } else {
            customerList = getCustomerList(hash);
            for(Customer customer: customerList) {
                List<TrackerLastState> customerTrackerLastStateList = getTrackerStateList(customer.getId().toString(),trackerList,hash);
                if(customerTrackerLastStateList != null) {
                    trackerLastStateList.addAll(customerTrackerLastStateList);
                }
            }
        }

        for(Tracker tracker: trackerList) {
            //this should be turned to a function
            TrackerState trackerState = new TrackerState();
            trackerState.setLabel(tracker.getLabel());
            trackerState.setCustomerId(tracker.getUserId().toString());
            trackerState.setTrackerId(tracker.getId().toString());
            trackerState.setImei(tracker.getSource().getDeviceId());
            trackerState.setModel(tracker.getSource().getModel());
            trackerState.setPhoneNumber(tracker.getSource().getPhone());
            trackerState.setConnectionStatus(tracker.getSource().getConnectionStatus());
            trackerState.setTariffEndDate(tracker.getSource().getTariffEndDate());
            Customer trackerCustomer = getCustomer(hash,tracker.getUserId().toString());
            System.out.println(trackerCustomer);
            if(trackerCustomer != null){
                String customerName = trackerCustomer.getFirstName() +" "+ trackerCustomer.getMiddleName() + " " + trackerCustomer.getLastName();
                trackerState.setCustomerName(customerName);
            }

            //did not want to go back to server
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

        return new LinkedHashSet<>(filterTrackers(duration,type,order, trackerStates));
    }

    //serves api/tracker/{id}
    public TrackerState getTracker(String id) throws IOException, UnirestException {
        TrackerState trackerState = new TrackerState();
        String hash = dealerAuthClient.getDealerHash();
        Tracker tracker = panelTrackersService.getTracker(hash, id);
        String customerHash = customerService.getCustomerHash(hash, tracker.getUserId().toString());
        ArrayList<String> trackerIdList = new ArrayList<String>();
        trackerIdList.add(id);
        TrackerLastState trackerLastState = apiTrackerService.getTrackerLastState(customerHash,trackerIdList).get(0);

        trackerState.setLabel(tracker.getLabel());
        trackerState.setCustomerId(tracker.getUserId().toString());
        trackerState.setTrackerId(tracker.getId().toString());
        trackerState.setImei(tracker.getSource().getDeviceId());
        trackerState.setModel(tracker.getSource().getModel());
        trackerState.setPhoneNumber(tracker.getSource().getPhone());
        trackerState.setConnectionStatus(tracker.getSource().getConnectionStatus());
        trackerState.setTariffEndDate(tracker.getSource().getTariffEndDate());
        Customer trackerCustomer = getCustomer(hash, tracker.getUserId().toString());
        System.out.println(trackerCustomer);

        if(trackerCustomer != null){
            String customerName = trackerCustomer.getFirstName() +" "+ trackerCustomer.getMiddleName() + " " + trackerCustomer.getLastName();
            trackerState.setCustomerName(customerName);
        }

        if(trackerLastState != null) {
            if(trackerLastState.getGps() != null) {
                if(trackerLastState.getGps().getUpdated() != null) {
                    trackerState.setLastGpsUpdate(trackerLastState.getGps().getUpdated());
                }
                if(trackerLastState.getGps().getSignalLevel() != null){
                    trackerState.setLastGpsSignalLevel(trackerLastState.getGps().getSignalLevel().toString());
                }
                if(trackerLastState.getGps().getLocation().getLat() != null) {
                    trackerState.setLastGpsLatitude(trackerLastState.getGps().getLocation().getLat().toString());
                }
                if(trackerLastState.getGps().getLocation().getLng() != null) {
                    trackerState.setLastGpsLongitude(trackerLastState.getGps().getLocation().getLng().toString());
                }
            }
            if(trackerLastState.getGsm() != null) {
                if(trackerLastState.getGsm().getSignalLevel() != null) {
                    trackerState.setGsmSignalLevel(trackerLastState.getGsm().getSignalLevel().toString());
                }
                if(trackerLastState.getGsm().getNetworkName() != null){
                    trackerState.setGsmNetworkName(trackerLastState.getGsm().getNetworkName());
                }
            }
            if(trackerLastState.getBatteryLevel() != null) {
                trackerState.setLastBatteryLevel(trackerLastState.getBatteryLevel().toString());
            }

        }
        return trackerState;
    }



    //filters list  of trackerStates
    public LinkedHashSet<TrackerState> filterTrackers(Optional<String> duration, Optional<String> type, Optional<String> order, Set<TrackerState>trackerStates) {
        order.orElse("dsc");
        //System.out.println("duration= " + duration.get() + " type= " + type.get() + "order= " + order.get() );

        if (duration.isPresent()) {
            System.out.println("Duration= " + duration.get());
            trackerStates = trackerStates.stream().filter(trackerState -> trackerState.getLastGpsUpdate() != null && trackerState.getLastGpsUpdate().substring(0,10).equals(duration.get())).collect(Collectors.toSet());
        }

        if (type.isPresent()){
            System.out.println("Type= " + type.get());
            trackerStates = trackerStates.stream().filter(trackerState -> trackerState.getModel().equals(type.get())).collect(Collectors.toSet());
        }
        if (order.isPresent()) {
            System.out.println(order.get());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            trackerStates = trackerStates.stream().sorted(Comparator.comparing(TrackerState::getLastGpsUpdate, (date1, date2) -> {
                try {
                    if(date1 != null & date2 != null) {
                        Date d1 = sdf.parse(date1);
                        Date d2 = sdf.parse(date2);
                        if(order.get().equals("dsc")) {
                            return (d1.getTime() > d2.getTime() ? -1 : 1); //descending
                        } else {
                            return (d1.getTime() > d2.getTime() ? 1 : -1); //ascending
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(date1 == null || date2 == null) {
                    return 0;
                }
                return date2.compareTo(date1);
            })).collect(Collectors.toCollection(LinkedHashSet::new));
        }

        System.out.println(trackerStates);
        return new LinkedHashSet<>(trackerStates);
    }

    //returns a list of trackers which may belong to a customer
    public List<Tracker> getTrackerList(Optional<String> customerId, String hash) throws IOException {
        System.out.println(customerId);
        return panelTrackersService.getTrackerList(hash, customerId);
    }

    //returns a list of customers from server2
    public List<Customer> getCustomerList(String hash) throws IOException {
        return customerService.getCustomers(hash);
    }

    //returns a customer with an Id = customerId
    public Customer getCustomer(String hash, String customerId) throws IOException {
        return customerService.getCustomer(hash, customerId);
    }

    //returns the last recorded tracker state from server 2
    public List<TrackerLastState> getTrackerStateList(String customerId, List<Tracker> trackers, String hash) throws IOException {
        String customerHash = customerService.getCustomerHash(hash, customerId);
        List<String> trackerIds = getCustomerTrackerIdList(trackers, customerId);
        if(trackerIds.size() > 0) {
            return apiTrackerService.getTrackerLastState(customerHash, trackerIds);
        }
        return null;
    }

    //Bundles all trackerId for a particular customer into a list
    public List<String> getCustomerTrackerIdList(List<Tracker> trackers, String customerId) {

        List<Tracker> customerTrackers = trackers.stream().filter(tracker -> tracker.getUserId().toString().equals(customerId)).collect(Collectors.toList());
        List<String> customerTrackerIds = new ArrayList<String>();
        for(Tracker customerTracker: customerTrackers) {
            if(!customerTracker.getDeleted() && !customerTracker.getSource().getBlocked()) { //note that IDs of deleted/hidden trackers are still returned hence their state cannot be retrieved
                customerTrackerIds.add(customerTracker.getId().toString());
            }
        }
        return customerTrackerIds;
    }

}