package com.nsoroma.trackermonitoring.services;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.nsoroma.trackermonitoring.datasourceclient.api.client.ApiTrackerServiceImpl;
import com.nsoroma.trackermonitoring.datasourceclient.api.model.Gps;
import com.nsoroma.trackermonitoring.datasourceclient.api.model.Gsm;
import com.nsoroma.trackermonitoring.datasourceclient.api.model.Location;
import com.nsoroma.trackermonitoring.datasourceclient.api.model.TrackerLastState;
import com.nsoroma.trackermonitoring.datasourceclient.panelAPI.client.PanelApiAuthentication;
import com.nsoroma.trackermonitoring.datasourceclient.panelAPI.client.PanelApiCustomerServiceImpl;
import com.nsoroma.trackermonitoring.datasourceclient.panelAPI.client.PanelApiTrackerServiceImpl;
import com.nsoroma.trackermonitoring.datasourceclient.panelAPI.model.Customer;
import com.nsoroma.trackermonitoring.datasourceclient.panelAPI.model.Source;
import com.nsoroma.trackermonitoring.datasourceclient.panelAPI.model.Tracker;
import com.nsoroma.trackermonitoring.model.trackerstate.TrackerState;
import com.nsoroma.trackermonitoring.repository.TrackerStateRepository;
import com.nsoroma.trackermonitoring.serviceutils.TrackerStateUtils;
import com.nsoroma.trackermonitoring.serviceutils.TrackerStateUtilsImpl;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.contains;

public class TrackersTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private PanelApiAuthentication dealerAuthClient;
    private PanelApiCustomerServiceImpl customerService;
    private PanelApiTrackerServiceImpl panelApiTrackerService;
    private ApiTrackerServiceImpl apiTrackerService;
    private TrackerStateRepository trackerStateRepository;
    private Trackers trackers;



    @Before
    public void setUp() {
        dealerAuthClient = mock(PanelApiAuthentication.class);
        customerService = mock(PanelApiCustomerServiceImpl.class);
        panelApiTrackerService = mock(PanelApiTrackerServiceImpl.class);
        apiTrackerService = mock(ApiTrackerServiceImpl.class);
        trackerStateRepository = mock(TrackerStateRepository.class);
        trackers = spy(new Trackers());
        TrackerStateUtilsImpl trackerStateUtils = new TrackerStateUtilsImpl();

        trackers.setDealerAuthClient(dealerAuthClient);
        trackers.setCustomerService(customerService);
        trackers.setPanelTrackersService(panelApiTrackerService);
        trackers.setApiTrackerService(apiTrackerService);
        trackers.setTrackerStateRepository(trackerStateRepository);
        trackers.setTrackerStateUtils(trackerStateUtils);


    }

    @Test
    public void getTrackersWithSpecifiedCustomerID() throws IOException, UnirestException {
        Integer customerId = 657483;
        Integer trackerId = 12345;
        String dealerHash = "dealerHash";
        String customerHash = "customerHash";

        List<Tracker> trackerList = Collections.singletonList(mockedTracker(customerId, trackerId));
        Customer mockedCustomer = mockedCustomer(customerId);
        List<TrackerLastState> trackerLastStates = Collections.singletonList(mockTrackerLastState(trackerId));
        List<String> trackerIds = Collections.singletonList(trackerId.toString());


        when(dealerAuthClient.getDealerHash()).thenReturn(dealerHash);
        when(customerService.getCustomerHash(dealerHash,customerId.toString())).thenReturn(customerHash);
        when(apiTrackerService.getTrackerLastState(customerHash, trackerIds)).thenReturn(trackerLastStates);
        when(panelApiTrackerService.getTrackerList(dealerHash, Optional.of(customerId.toString()))).thenReturn(trackerList);
        when(customerService.getCustomer(dealerHash, customerId.toString())).thenReturn(mockedCustomer);

        LinkedHashSet<TrackerState> trackerStates =  trackers.getTrackers(Optional.empty(),Optional.empty(), Optional.of(customerId.toString()),
                Optional.empty(), Optional.empty(), Optional.empty());

        verify(trackerStateRepository, never()).findAll();
        assertTrue(EqualsBuilder.reflectionEquals(mockedTrackerState(customerId.toString(),trackerId.toString()), trackerStates.stream().findFirst().get()));
    }



    @Test
    public void getAllTrackersWithoutACustomerId() throws IOException, UnirestException {
        String trackerId = "12345";
        String someCustomer = "someCustomer";

        when(trackerStateRepository.findAll()).thenReturn(Collections.singletonList(mockedTrackerState(someCustomer, trackerId)));

        LinkedHashSet<TrackerState> trackerStates =  trackers.getTrackers(Optional.empty(),Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty());

        verify(trackerStateRepository).findAll();
        assertTrue(EqualsBuilder.reflectionEquals(mockedTrackerState(someCustomer,trackerId), trackerStates.stream().findFirst().get()));

    }


    @Test
    public void filterTrackerStatesAscendingOrder() {
        String trackerId = "12345";
        String filterStartDate = "2020-02-15 11:53:56";
        String filterEndDate = "2020-03-29 11:53:56";
        String filterType = "someModel";
        String filterStatus = "someConnectionStatus";
        String order = "asc";

        Set<TrackerState> trackerStateList  = new HashSet<>(Collections.singleton(mockedTrackerState("someCustomer", trackerId)));

        LinkedHashSet<TrackerState> trackerStates = trackers.filterTrackers(Optional.of(filterStartDate), Optional.of(filterEndDate), Optional.of(filterType),
                Optional.of(order), trackerStateList, Optional.of(filterStatus));

        assertTrue(EqualsBuilder.reflectionEquals(trackerStateList.stream().findFirst(), trackerStates.stream().findFirst()));
    }


    @Test
    public void getAllTrackerStates() throws IOException, UnirestException {
        Integer customerId = 657483;
        Integer trackerId = 12345;
        String dealerHash = "dealerHash";
        String customerHash = "customerHash";

        List<Tracker> trackerList = Collections.singletonList(mockedTracker(customerId, trackerId));
        List<Customer> customerList = Collections.singletonList(mockedCustomer(customerId));
        List<String> trackerIds = Collections.singletonList(trackerId.toString());
        List<TrackerLastState> trackerLastStates = Collections.singletonList(mockTrackerLastState(trackerId));

        when(dealerAuthClient.getDealerHash()).thenReturn(dealerHash);
        when(customerService.getCustomers(dealerHash)).thenReturn(customerList);
        when(panelApiTrackerService.getTrackerList(dealerHash, Optional.empty())).thenReturn(trackerList);
        when(panelApiTrackerService.getTrackerList(dealerHash, Optional.of(customerId.toString()))).thenReturn(trackerList);
        when(customerService.getCustomerHash(dealerHash,customerId.toString())).thenReturn(customerHash);
        when(apiTrackerService.getTrackerLastState(customerHash, trackerIds)).thenReturn(trackerLastStates);

        LinkedHashSet<TrackerState> trackerStates =  trackers.getAllTrackerStates();

        assertTrue(EqualsBuilder.reflectionEquals(mockedTrackerState(customerId.toString(),trackerId.toString()), trackerStates.stream().findFirst().get()));
    }

    @Test
    public void getTracker() throws IOException, UnirestException {
        Integer customerId = 657483;
        Integer trackerId = 12345;
        String dealerHash = "dealerHash";
        String customerHash = "customerHash";

        Tracker mockedTracker = mockedTracker(customerId, trackerId);
        Customer mockedCustomer = mockedCustomer(customerId);
        List<TrackerLastState> trackerLastStateList = Collections.singletonList(mockTrackerLastState(trackerId));
        List<String> trackerIdList = Collections.singletonList(trackerId.toString());

        when(dealerAuthClient.getDealerHash()).thenReturn(dealerHash);
        when(panelApiTrackerService.getTracker(dealerHash,trackerId.toString())).thenReturn(mockedTracker);
        when(customerService.getCustomerHash(dealerHash, customerId.toString())).thenReturn(customerHash);
        when(customerService.getCustomer(dealerHash, customerId.toString())).thenReturn(mockedCustomer);
        when(apiTrackerService.getTrackerLastState(customerHash, trackerIdList)).thenReturn(trackerLastStateList);

        TrackerState trackerStates =  trackers.getTracker(trackerId.toString());

        assertTrue(EqualsBuilder.reflectionEquals(mockedTrackerState(customerId.toString(), trackerId.toString()), trackerStates));
    }

    @Test
    public void getTrackerByImei() throws IOException, UnirestException {
        Integer trackerImei = 123456789;
        Integer customerId = 657483;
        Integer trackerId = 12345;
        String dealerHash = "dealerHash";
        String customerHash = "customerHash";

        List<Tracker> trackerList = Collections.singletonList(mockedTracker(customerId, trackerId));
        Customer mockedCustomer = mockedCustomer(customerId);
        List<TrackerLastState> trackerLastStateList = Collections.singletonList(mockTrackerLastState(trackerId));
        List<String> trackerIdList = Collections.singletonList(trackerId.toString());

        when(dealerAuthClient.getDealerHash()).thenReturn(dealerHash);
        when(panelApiTrackerService.getTrackerList(dealerHash, Optional.empty())).thenReturn(trackerList);
        when(customerService.getCustomerHash(dealerHash, customerId.toString())).thenReturn(customerHash);
        when(customerService.getCustomer(dealerHash, customerId.toString())).thenReturn(mockedCustomer);
        when(apiTrackerService.getTrackerLastState(customerHash, trackerIdList)).thenReturn(trackerLastStateList);

        TrackerState trackerState =  trackers.getTrackerByImei(trackerImei.toString());

        assertTrue(EqualsBuilder.reflectionEquals(mockedTrackerState(customerId.toString(), trackerId.toString()), trackerState));
    }




    //mocking data
    private Customer mockedCustomer(Integer customerId) {
        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setFirstName("testFirstName");
        customer.setLastName("testLastName");
        customer.setMiddleName("testMiddleName");
        customer.setLogin("testLogin");
        customer.setDealerId(598764);
        return customer;
    }

    private Tracker mockedTracker(Integer customerId, Integer trackerId) {
        Source trackerSource = new Source();
        trackerSource.setBlocked(false);
        trackerSource.setId(trackerId);
        trackerSource.setDeviceId("123456789");
        trackerSource.setModel("someModel");
        trackerSource.setPhone("0233446456");
        trackerSource.setConnectionStatus("someConnectionStatus");
        trackerSource.setTariffEndDate("someTariffEndDate");

        Tracker tracker = new Tracker();
        tracker.setId(trackerId);
        tracker.setDealerId(67890);
        tracker.setUserId(customerId);
        tracker.setLabel("GV-34334");
        tracker.setSource(trackerSource);
        tracker.setDeleted(false);

        return tracker;
    }

    private TrackerLastState mockTrackerLastState(Integer trackerId) {
        Location location = new Location();
        location.setLat(0.1241234);
        location.setLng(-1.2342344);

        Gps gps = new Gps();
        gps.setUpdated("2020-01-30 11:53:56");
        gps.setSignalLevel(100);
        gps.setLocation(location);

        Gsm gsm = new Gsm();
        gsm.setUpdated("2020-02-25 11:53:56");
        gsm.setNetworkName("MTN");
        gsm.setSignalLevel(89);

        TrackerLastState trackerLastState = new TrackerLastState();
        trackerLastState.setTrackerId(trackerId.toString());
        trackerLastState.setGps(gps);
        trackerLastState.setGsm(gsm);
        trackerLastState.setBatteryLevel(60);

        return trackerLastState;
    }

    private TrackerState mockedTrackerState(String customerId, String trackerId) {
        TrackerState trackerState = new TrackerState();
        trackerState.setLabel("GV-34334");
        trackerState.setTrackerId(trackerId);
        trackerState.setCustomerId(customerId);
        trackerState.setImei("123456789");
        trackerState.setModel("someModel");
        trackerState.setPhoneNumber("0233446456");
        trackerState.setConnectionStatus("someConnectionStatus");
        trackerState.setTariffEndDate("someTariffEndDate");
        trackerState.setCustomerName("testFirstName testMiddleName testLastName");
        trackerState.setLastGpsUpdate("2020-01-30 11:53:56");
        trackerState.setLastGsmUpdate("2020-02-25 11:53:56");
        trackerState.setLastGpsSignalLevel("100");
        trackerState.setLastGpsLatitude("0.1241234");
        trackerState.setLastGpsLongitude("-1.2342344");
        trackerState.setLastBatteryLevel("60");
        trackerState.setGsmSignalLevel("89");
        trackerState.setGsmNetworkName("MTN");

        return trackerState;

    }
}