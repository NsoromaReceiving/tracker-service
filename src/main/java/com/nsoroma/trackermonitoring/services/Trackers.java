package com.nsoroma.trackermonitoring.services;


import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.nsoroma.trackermonitoring.datasourceclient.server1api.client.LocationManager;
import com.nsoroma.trackermonitoring.datasourceclient.server1api.client.UnitManager;
import com.nsoroma.trackermonitoring.datasourceclient.server1api.model.LatestLocation;
import com.nsoroma.trackermonitoring.datasourceclient.server1api.model.Unit;
import com.nsoroma.trackermonitoring.datasourceclient.server2api.client.ApiTrackerService;
import com.nsoroma.trackermonitoring.datasourceclient.server2api.model.TrackerLastState;
import com.nsoroma.trackermonitoring.datasourceclient.server2panelapi.client.PanelApiAuthentication;
import com.nsoroma.trackermonitoring.datasourceclient.server2panelapi.client.PanelApiCustomerService;
import com.nsoroma.trackermonitoring.datasourceclient.server2panelapi.client.PanelApiTrackerService;
import com.nsoroma.trackermonitoring.datasourceclient.server2panelapi.model.Customer;
import com.nsoroma.trackermonitoring.datasourceclient.server2panelapi.model.Tracker;
import com.nsoroma.trackermonitoring.exceptions.DataSourceClientResponseException;
import com.nsoroma.trackermonitoring.model.trackerstate.TrackerState;

import com.nsoroma.trackermonitoring.repository.TrackerStateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class Trackers {

    private Logger log = LoggerFactory.getLogger(Trackers.class);

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

    @Autowired
    private LocationManager locationManager;

    @Autowired
    private UnitManager unitManager;

    //serves api/trackers/?param1=&param2=...
    public LinkedHashSet<TrackerState> getTrackers(Optional<String> startDate, Optional<String> endDate, Optional<String> customerId,
                                                                     Optional<String> type, Optional<String> order, Optional<String> status, Optional<String> server) throws IOException, UnirestException {
        log.info("getTracker Called");
        Set<TrackerState> trackerStates = new HashSet<>(Collections.emptySet());

        /*if(customerId.isPresent()){
            log.info("Customer Id present");
            String hash = dealerAuthClient.getDealerHash();
            List<Tracker> trackerList = getTrackerList(customerId, hash); //gets list of all trackers on server 2 which may belong to a user
            List<TrackerLastState> customerTrackerLastStateList = getTrackerStateList(customerId.get(),trackerList,hash); //list od last tracker state for customer
            Customer customer = getCustomer(hash, customerId.get()); // get customer details
            List<Tracker> customerTrackers = trackerList.parallelStream().filter(tracker ->
                    tracker.getUserId().toString().equals(customer.getId().toString())).collect(Collectors.toList()); //filters for trackers that belong to this customer ID

            if (!customerTrackerLastStateList.isEmpty()) {
                for (TrackerLastState trackerLastState: customerTrackerLastStateList) {
                    TrackerState trackerState = new TrackerState();
                    trackerStates.add(trackerStateData(trackerState,customerTrackers,trackerLastState,customer)); // sets the trackerState data and adds to List
                }
            }
        } else {
            trackerStates.addAll(trackerStateRepository.findAll());

        }

        return new LinkedHashSet<>(filterTrackers(startDate, endDate, type,order, trackerStates, status, server));*/

        trackerStates.addAll(trackerStateRepository.findAll());
        return new LinkedHashSet<>(filterTrackers(startDate, endDate, type,order, trackerStates, status, server, customerId));
    }


    //get all trackers' states
    public LinkedHashSet<TrackerState> getServerTwoTrackerStates() throws IOException, UnirestException {
        log.info("getAllTrackerStates Called");
        String hash = dealerAuthClient.getDealerHash();
        List<Tracker> trackerList = getTrackerList(Optional.empty(), hash); //gets list of all trackers on server 2 which may belong to a user
        List<Customer> customerList = getCustomerList(hash);
        Set<TrackerState> trackerStates = new HashSet<>(Collections.emptySet());

        for(Customer customer: customerList) {
            List<TrackerLastState> customerTrackerLastStateList = getTrackerStateList(customer.getId().toString(), trackerList, hash);
            List<Tracker> customerTrackers = trackerList.parallelStream().filter(tracker -> tracker.getUserId().toString().equals(customer.getId().toString())).collect(Collectors.toList());
            if (!customerTrackerLastStateList.isEmpty()) {
                for (TrackerLastState trackerLastState : customerTrackerLastStateList) {
                    TrackerState trackerState = new TrackerState();
                    trackerStates.add(trackerStateData(trackerState, customerTrackers, trackerLastState, customer));
                }
            }
        }

        return new LinkedHashSet<>(trackerStates);
    }


    //get server1 Tracker state
    public LinkedHashSet<TrackerState> getServerOneTrackerStates() throws DataSourceClientResponseException, UnirestException, IOException {
        Set<TrackerState> trackerStates = new HashSet<>(Collections.emptySet());
        List<Unit> unitList = unitManager.getUnits();
        List<LatestLocation> latestLocationList = locationManager.getLatestLocation();
        System.out.println(unitList.size());
        System.out.println(latestLocationList.size());
        int len = 0;
        for(Unit unit: unitList) {
            if(!unit.getImei().equals("")) {
               LatestLocation latestLocation = latestLocationList.parallelStream().filter(latestLocation1 -> unit.getImei().equals(latestLocation1.getImei())).findFirst().orElse(null);
                if (latestLocation != null) {
                    trackerStates.add(setServer1TrackerStateData(latestLocation, unit));
                }
            }

        }
        return new LinkedHashSet<>(trackerStates);
    }

    //get tracker by Id in server two
    public TrackerState getServerTwoTracker(String id) throws IOException, UnirestException {
        log.info("getTracker called");
        TrackerState trackerState = new TrackerState();
        String hash = dealerAuthClient.getDealerHash();
        Tracker tracker = panelTrackersService.getTracker(hash, id);
        String customerHash = customerService.getCustomerHash(hash, tracker.getUserId().toString());
        ArrayList<String> trackerIdList = new ArrayList<>();
        trackerIdList.add(id);
        TrackerLastState trackerLastState = apiTrackerService.getTrackerLastState(customerHash,trackerIdList).get(0);

        return setTrackerStateData(tracker,trackerLastState,trackerState,hash);
    }

    //get tracker by Id in server one
    public Optional<TrackerState> getServerOneTracker(String id) {
        return trackerStateRepository.findById(id);
    }

    //get tracker by IMEI
    public TrackerState getTrackerByImei(String imei) throws IOException, UnirestException {
        log.info("getTrackerByImei called");
        TrackerState trackerState = new TrackerState();
        String hash = dealerAuthClient.getDealerHash();
        List<Tracker> trackerList = panelTrackersService.getTrackerList(hash, Optional.empty());
        Tracker tracker = trackerList.stream().filter(trackers -> trackers.getSource().getDeviceId().equals(imei)).toArray(Tracker[]::new)[0];
        String customerHash = customerService.getCustomerHash(hash, tracker.getUserId().toString());
        ArrayList<String> trackerIdList = new ArrayList<>();
        trackerIdList.add(tracker.getId().toString());
        TrackerLastState trackerLastState = apiTrackerService.getTrackerLastState(customerHash,trackerIdList).get(0);

        return setTrackerStateData(tracker,trackerLastState,trackerState,hash);
    }




    /********helper functions ********/


    //filters list  of trackerStates
    protected LinkedHashSet<TrackerState> filterTrackers(Optional<String> startDate, Optional<String> endDate, Optional<String> type,
                                                         Optional<String> order, Set<TrackerState> trackerStates, Optional<String> status, Optional<String> server, Optional<String> customerId) {
        if (customerId.isPresent()) {
            log.info("CustomerId : {}", customerId.get());
            trackerStates = trackerStates.stream().filter(trackerState -> trackerState.getCustomerId().equals(customerId.get())).collect(Collectors.toSet());
        }

        if (server.isPresent()) {
            log.info("Server : {}", server.get());
            trackerStates = trackerStates.stream().filter(trackerState -> trackerState.getServer().equals(server.get())).collect(Collectors.toSet());
        }

        //new code start date
        String datePattern = "yyyy-MM-dd HH:mm:ss";
        if(startDate.isPresent()) {
            log.info("Start Date : {}.", startDate.get());
            SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
            trackerStates = trackerStates.stream().filter(trackerState -> {
                try {
                    return trackerState.getLastGpsUpdate() != null &&
                            sdf.parse(trackerState.getLastGpsUpdate()).after(sdf.parse(startDate.get()));
                } catch (ParseException e) {
                    return false;
                }

            }).collect(Collectors.toSet());
        }

        //new code end date
        if(endDate.isPresent()) {
            log.info("End Date : {}.", endDate.get());
            SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
            trackerStates = trackerStates.stream().filter(trackerState -> {
                try {
                    return trackerState.getLastGpsUpdate() != null &&
                            sdf.parse(trackerState.getLastGpsUpdate()).before(sdf.parse(endDate.get()));
                } catch (ParseException e) {
                    return false;
                }
            }).collect(Collectors.toSet());
        }

        // filter type
        if (type.isPresent()){
            log.info("Type : {}.", type.get());
            trackerStates = trackerStates.stream().filter(trackerState -> trackerState.getModel().equals(type.get())).collect(Collectors.toSet());
        }

        //filter status
        if(status.isPresent()) {
            log.info("Status : {}.", status.get());
            trackerStates = trackerStates.stream().filter(trackerState -> trackerState.getConnectionStatus().equals(status.get())).collect(Collectors.toSet());
        }

        //filter order
        if (order.isPresent()) {
            log.info("Arrangement Order : {}.", order.get());
            SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
            trackerStates = trackerStates.stream().sorted(Comparator.comparing(TrackerState::getLastGpsUpdate, (date1, date2) -> {
                try {
                    if(date1 != null && date2 != null) {
                        Date d1 = sdf.parse(date1);
                        Date d2 = sdf.parse(date2);
                        if(order.get().equals("dsc")) {
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
        }
        return new LinkedHashSet<>(trackerStates);
    }

    //returns a list of trackers which may belong to a customer
    protected List<Tracker> getTrackerList(Optional<String> customerId, String hash) throws IOException, UnirestException {
        return panelTrackersService.getTrackerList(hash, customerId);
    }

    //returns a list of customers from server2
    protected List<Customer> getCustomerList(String hash) throws IOException, UnirestException {
        return customerService.getCustomers(hash);
    }

    //returns a customer with an Id = customerId
    protected Customer getCustomer(String hash, String customerId) throws IOException, UnirestException {
        return customerService.getCustomer(hash, customerId);
    }

    //returns the last recorded tracker state from server 2
    protected List<TrackerLastState> getTrackerStateList(String customerId, List<Tracker> trackers, String hash) throws IOException, UnirestException {
        String customerHash = customerService.getCustomerHash(hash, customerId);
        List<String> trackerIds = getCustomerTrackerIdList(trackers, customerId);
        List<TrackerLastState> trackerLastStateList = new ArrayList<>();
        if(!trackerIds.isEmpty()) {
            trackerLastStateList = apiTrackerService.getTrackerLastState(customerHash, trackerIds);
        }
        return trackerLastStateList;
    }

    //Bundles all trackerId for a particular customer into a list
    private List<String> getCustomerTrackerIdList(List<Tracker> trackers, String customerId) {
        List<Tracker> customerTrackers = trackers.stream().filter(tracker -> tracker.getUserId().toString().equals(customerId)).collect(Collectors.toList());
        List<String> customerTrackerIds = new ArrayList<>();
        for(Tracker customerTracker: customerTrackers) {
            if(!customerTracker.getDeleted() && !customerTracker.getSource().getBlocked()) { //note that IDs of deleted/hidden trackers are still returned hence their state cannot be retrieved
                customerTrackerIds.add(customerTracker.getId().toString());
            }
        }
        return customerTrackerIds;
    }


    //Sets TrackerState Object values
    private TrackerState setTrackerStateData(Tracker tracker, TrackerLastState trackerLastState, TrackerState trackerState, String hash) throws IOException, UnirestException {
        trackerState.setServer("2");
        trackerState.setLabel(tracker.getLabel());
        trackerState.setCustomerId(tracker.getUserId().toString());
        trackerState.setTrackerId(tracker.getId().toString());
        trackerState.setImei(tracker.getSource().getDeviceId());
        trackerState.setModel(tracker.getSource().getModel());
        trackerState.setPhoneNumber(tracker.getSource().getPhone());
        trackerState.setConnectionStatus(tracker.getSource().getConnectionStatus());
        trackerState.setTariffEndDate(tracker.getSource().getTariffEndDate());
        Customer trackerCustomer = getCustomer(hash, tracker.getUserId().toString());

        if(trackerCustomer != null){
            String customerName = trackerCustomer.getFirstName() +" "+ trackerCustomer.getMiddleName() + " " + trackerCustomer.getLastName();
            trackerState.setCustomerName(customerName);
        }

        if(trackerLastState != null) {
            setGpsGsmValues(trackerState, trackerLastState);

        }

        return trackerState;
    }


    private TrackerState trackerStateData(TrackerState trackerState, List<Tracker> customerTrackers, TrackerLastState trackerLastState, Customer customer) {
        List<Tracker> trackerList1 = customerTrackers.parallelStream().filter(tracker1 -> tracker1.getId().toString().equals(trackerLastState.getTrackerId())).collect(Collectors.toList());
        if (!trackerList1.isEmpty()) {
            trackerState.setServer("2");
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

            setGpsGsmValues(trackerState, trackerLastState);
        }
        return trackerState;
    }

    private void setGpsGsmValues(TrackerState trackerState, TrackerLastState trackerLastState) {
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


    private TrackerState setServer1TrackerStateData(LatestLocation latestLocation, Unit unit) throws IOException, UnirestException, DataSourceClientResponseException {
           TrackerState trackerState = new TrackerState();
           trackerState.setServer("1");
           trackerState.setTrackerId(latestLocation.getImei());
           trackerState.setImei(latestLocation.getImei());
           trackerState.setLabel(latestLocation.getName());
           trackerState.setLastGpsUpdate(latestLocation.getDateTime().replace("T", " "));
           trackerState.setLastGpsLongitude(latestLocation.getLongitude());
           trackerState.setLastGpsLatitude(latestLocation.getLatitude());
           trackerState.setCustomerName(unit.getGroupName());
           trackerState.setCustomerId(unit.getGroupName());
           trackerState.setModel("");
           if (unit.getStatus().equals("Active")) {
               trackerState.setConnectionStatus("active");
           } else {
               trackerState.setConnectionStatus("offline");
           }

           return trackerState;

    }


    public void setDealerAuthClient(PanelApiAuthentication dealerAuthClient) {
        this.dealerAuthClient = dealerAuthClient;
    }

    public void setCustomerService(PanelApiCustomerService customerService) {
        this.customerService = customerService;
    }

    public void setPanelTrackersService(PanelApiTrackerService panelTrackersService) {
        this.panelTrackersService = panelTrackersService;
    }

    public void setApiTrackerService(ApiTrackerService apiTrackerService) {
        this.apiTrackerService = apiTrackerService;
    }

    public void setTrackerStateRepository(TrackerStateRepository trackerStateRepository) {
        this.trackerStateRepository = trackerStateRepository;
    }


}