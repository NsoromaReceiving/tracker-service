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

import com.nsoroma.trackermonitoring.repository.TrackerStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class Trackers {

    @Autowired
    private
    PanelApiAuthentication dealerAuthClient;

    @Autowired
    private
    PanelApiCustomerService customerService;

    @Autowired
    private
    PanelApiTrackerService panelTrackersService;

    @Autowired
    private
    ApiTrackerService apiTrackerService;

    @Autowired
    private TrackerStateRepository trackerStateRepository;

    public Trackers(){}

    //serves api/trackers/?param1=&param2=...
    public LinkedHashSet<TrackerState> getTrackers(Optional<String> startDate, Optional<String> endDate, Optional<String> customerId,
                                                                     Optional<String> type, Optional<String> order, Optional<String> status) throws IOException {

        String hash = dealerAuthClient.getDealerHash();
        List<Tracker> trackerList = getTrackerList(customerId, hash); //gets list of all trackers on server 2 which may belong to a user
        List<Customer> customerList = getCustomerList(hash);
        Set<TrackerState> trackerStates = new HashSet<>(Collections.emptySet());

        if(customerId.isPresent()){
            List<TrackerLastState> customerTrackerLastStateList = getTrackerStateList(customerId.get(),trackerList,hash);
            Customer customer = getCustomer(hash, customerId.get());
            List<Tracker> customerTrackers = trackerList.parallelStream().filter(tracker -> tracker.getUserId().toString().equals(customer.getId().toString())).collect(Collectors.toList());
            if (customerTrackerLastStateList != null && customerTrackerLastStateList.size() > 0) {
                for (TrackerLastState trackerLastState: customerTrackerLastStateList) {
                    TrackerState trackerState = new TrackerState();
                    trackerStates.add(trackerStateData(trackerState,customerTrackers,trackerLastState,customer));
                }
            }
        } else {
            //old method
            /*for(Customer customer: customerList) {
                List<TrackerLastState> customerTrackerLastStateList = getTrackerStateList(customer.getId().toString(),trackerList,hash);
                List<Tracker> customerTrackers = trackerList.parallelStream().filter(tracker -> tracker.getUserId().toString().equals(customer.getId().toString())).collect(Collectors.toList());
                if (customerTrackerLastStateList != null && customerTrackerLastStateList.size() > 0) {
                    for (TrackerLastState trackerLastState : customerTrackerLastStateList) {
                        TrackerState trackerState = new TrackerState();
                        trackerStates.add(trackerStateData(trackerState, customerTrackers, trackerLastState, customer));
                    }
                }

            }*/

            trackerStates.addAll(trackerStateRepository.findAll());

        }

        return new LinkedHashSet<>(filterTrackers(startDate, endDate, type,order, trackerStates, status));
    }


    //get all trackers' states
    public LinkedHashSet<TrackerState> getAllTrackerStates() throws IOException {
        String hash = dealerAuthClient.getDealerHash();
        List<Tracker> trackerList = getTrackerList(Optional.empty(), hash); //gets list of all trackers on server 2 which may belong to a user
        List<Customer> customerList = getCustomerList(hash);
        Set<TrackerState> trackerStates = new HashSet<>(Collections.emptySet());

        for(Customer customer: customerList) {
            List<TrackerLastState> customerTrackerLastStateList = getTrackerStateList(customer.getId().toString(), trackerList, hash);
            List<Tracker> customerTrackers = trackerList.parallelStream().filter(tracker -> tracker.getUserId().toString().equals(customer.getId().toString())).collect(Collectors.toList());
            if (customerTrackerLastStateList != null && customerTrackerLastStateList.size() > 0) {
                for (TrackerLastState trackerLastState : customerTrackerLastStateList) {
                    TrackerState trackerState = new TrackerState();
                    trackerStates.add(trackerStateData(trackerState, customerTrackers, trackerLastState, customer));
                }
            }
        }

        return new LinkedHashSet<>(trackerStates);
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

        return setTrackerStateData(tracker,trackerLastState,trackerState,hash);
    }

    //serves api/tracker/imei/{imei}
    public TrackerState getTrackerByImei(String imei) throws IOException, UnirestException {
        System.out.println(imei);
        TrackerState trackerState = new TrackerState();
        String hash = dealerAuthClient.getDealerHash();
        List<Tracker> trackerList = panelTrackersService.getTrackerList(hash, Optional.empty());
        Tracker tracker = trackerList.stream().filter(trackers -> trackers.getSource().getDeviceId().equals(imei)).toArray(Tracker[]::new)[0];
        String customerHash = customerService.getCustomerHash(hash, tracker.getUserId().toString());
        ArrayList<String> trackerIdList = new ArrayList<String>();
        trackerIdList.add(tracker.getId().toString());
        TrackerLastState trackerLastState = apiTrackerService.getTrackerLastState(customerHash,trackerIdList).get(0);

        return setTrackerStateData(tracker,trackerLastState,trackerState,hash);
    }




    /********helper functions ********/


    //filters list  of trackerStates
    private LinkedHashSet<TrackerState> filterTrackers(Optional<String> startDate, Optional<String> endDate, Optional<String> type, Optional<String> order, Set<TrackerState> trackerStates, Optional<String> status) {
        order.orElse("dsc");

        //old code
        /*if (startDate.isPresent() && endDate.isPresent()) {
            System.out.println("Start Date = " + startDate.get() + "End Date = " + endDate.get());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            trackerStates = trackerStates.stream().filter(trackerState -> {
                try {
                    return trackerState.getLastGpsUpdate() != null &&
                             sdf.parse(trackerState.getLastGpsUpdate()).after(sdf.parse(startDate.get())) &&
                            sdf.parse(trackerState.getLastGpsUpdate()).before(sdf.parse(endDate.get()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return false;
            }).collect(Collectors.toSet());
        }*/

        //new code start date
        if(startDate.isPresent()) {
            System.out.println("Start Date : " + startDate.get());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            trackerStates = trackerStates.stream().filter(trackerState -> {
                try {
                    return trackerState.getLastGpsUpdate() != null &&
                            sdf.parse(trackerState.getLastGpsUpdate()).after(sdf.parse(startDate.get()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return false;
            }).collect(Collectors.toSet());
        }

        //new code end date
        if(endDate.isPresent()) {
            System.out.println("End Date : " + endDate.get());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            trackerStates = trackerStates.stream().filter(trackerState -> {
                try {
                    return trackerState.getLastGpsUpdate() != null &&
                            sdf.parse(trackerState.getLastGpsUpdate()).before(sdf.parse(endDate.get()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return false;
            }).collect(Collectors.toSet());
        }

        // filter type
        if (type.isPresent()){
            System.out.println("Type= " + type.get());
            trackerStates = trackerStates.stream().filter(trackerState -> trackerState.getModel().equals(type.get())).collect(Collectors.toSet());
        }

        //filter status
        if(status.isPresent()) {
            System.out.println("Status = " + status.get());
            trackerStates = trackerStates.stream().filter(trackerState -> trackerState.getConnectionStatus().equals(status.get())).collect(Collectors.toSet());
        }

        //filter order
        if (order.isPresent()) {
            System.out.println("Arrangement Order = " + order.get());
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
    private List<Tracker> getTrackerList(Optional<String> customerId, String hash) throws IOException {
        System.out.println(customerId);
        return panelTrackersService.getTrackerList(hash, customerId);
    }

    //returns a list of customers from server2
    private List<Customer> getCustomerList(String hash) throws IOException {
        return customerService.getCustomers(hash);
    }

    //returns a customer with an Id = customerId
    private Customer getCustomer(String hash, String customerId) throws IOException {
        return customerService.getCustomer(hash, customerId);
    }

    //returns the last recorded tracker state from server 2
    private List<TrackerLastState> getTrackerStateList(String customerId, List<Tracker> trackers, String hash) throws IOException {
        String customerHash = customerService.getCustomerHash(hash, customerId);
        List<String> trackerIds = getCustomerTrackerIdList(trackers, customerId);
        if(trackerIds.size() > 0) {
            return apiTrackerService.getTrackerLastState(customerHash, trackerIds);
        }
        return null;
    }

    //Bundles all trackerId for a particular customer into a list
    private List<String> getCustomerTrackerIdList(List<Tracker> trackers, String customerId) {

        List<Tracker> customerTrackers = trackers.stream().filter(tracker -> tracker.getUserId().toString().equals(customerId)).collect(Collectors.toList());
        List<String> customerTrackerIds = new ArrayList<String>();
        for(Tracker customerTracker: customerTrackers) {
            if(!customerTracker.getDeleted() && !customerTracker.getSource().getBlocked()) { //note that IDs of deleted/hidden trackers are still returned hence their state cannot be retrieved
                customerTrackerIds.add(customerTracker.getId().toString());
            }
        }
        return customerTrackerIds;
    }


    //Sets TrackerState Object values
    private TrackerState setTrackerStateData(Tracker tracker, TrackerLastState trackerLastState, TrackerState trackerState, String hash) throws IOException {
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


    private TrackerState trackerStateData(TrackerState trackerState, List<Tracker> customerTrackers, TrackerLastState trackerLastState, Customer customer) {
        List<Tracker> trackerList1 = customerTrackers.parallelStream().filter(tracker1 -> tracker1.getId().toString().equals(trackerLastState.getTrackerId())).collect(Collectors.toList());
        if (trackerList1.size() > 0) {
            trackerState.setLabel(trackerList1.get(0).getLabel());
            trackerState.setCustomerId(trackerList1.get(0).getUserId().toString());
            trackerState.setTrackerId(trackerList1.get(0).getId().toString());
            trackerState.setImei(trackerList1.get(0).getSource().getDeviceId());
            trackerState.setModel(trackerList1.get(0).getSource().getModel());
            trackerState.setPhoneNumber(trackerList1.get(0).getSource().getPhone());
            trackerState.setConnectionStatus(trackerList1.get(0).getSource().getConnectionStatus());
            trackerState.setTariffEndDate(trackerList1.get(0).getSource().getTariffEndDate());
            String customerName = customer.getFirstName() + " " + customer.getMiddleName() + " " + customer.getLastName();
            trackerState.setCustomerName(customerName);

            if (trackerLastState.getGps() != null) {
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
            if (trackerLastState.getGsm() != null) {
                if (trackerLastState.getGsm().getSignalLevel() != null) {
                    trackerState.setGsmSignalLevel(trackerLastState.getGsm().getSignalLevel().toString());
                }
                if (trackerLastState.getGsm().getNetworkName() != null) {
                    trackerState.setGsmNetworkName(trackerLastState.getGsm().getNetworkName());
                }
            }
            if (trackerLastState.getBatteryLevel() != null) {
                trackerState.setLastBatteryLevel(trackerLastState.getBatteryLevel().toString());
            }
        }
        return trackerState;
    }

}