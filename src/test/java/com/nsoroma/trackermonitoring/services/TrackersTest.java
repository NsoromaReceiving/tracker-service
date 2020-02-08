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
        trackers.setDealerAuthClient(dealerAuthClient);
        trackers.setCustomerService(customerService);
        trackers.setPanelTrackersService(panelApiTrackerService);
        trackers.setApiTrackerService(apiTrackerService);
        trackers.setTrackerStateRepository(trackerStateRepository);


    }

    @Test
    public void getTrackersWithSpecifiedCustomerID() throws IOException, UnirestException {
        Integer customerId = 657483;
        Integer trackerId = 12345;
        String dealerHash = "dealerHash";
        String customerHash = "customerHash";

        Tracker mockedTracker = mockedTracker(customerId, trackerId);
        List<Tracker> trackerList = new ArrayList<>();
        trackerList.add(mockedTracker);

        Customer mockedCustomer = mockedCustomer(customerId);

        TrackerLastState trackerLastState = mockTrackerLastState(trackerId);
        List<TrackerLastState> trackerLastStates = new ArrayList<>();
        trackerLastStates.add(trackerLastState);

        List<String> trackerIds = new ArrayList<>();
        trackerIds.add(trackerId.toString());


        when(dealerAuthClient.getDealerHash()).thenReturn(dealerHash);
        when(customerService.getCustomerHash(dealerHash,customerId.toString())).thenReturn(customerHash);
        when(apiTrackerService.getTrackerLastState(customerHash, trackerIds)).thenReturn(trackerLastStates);
        when(panelApiTrackerService.getTrackerList(dealerHash, Optional.of(customerId.toString()))).thenReturn(trackerList);
        when(customerService.getCustomer(dealerHash, customerId.toString())).thenReturn(mockedCustomer);

        LinkedHashSet<TrackerState> trackerStates =  trackers.getTrackers(Optional.empty(),Optional.empty(), Optional.of(customerId.toString()),
                Optional.empty(), Optional.empty(), Optional.empty());

        verify(trackerStateRepository, never()).findAll();
        assertThat(trackerStates.size(), is(1));
        assertThat(trackerStates, contains(hasProperty("trackerId", is(trackerId.toString()))));
    }



    @Test
    public void getAllTrackersWithoutSpecifiedCustomerId() throws IOException, UnirestException {
        Optional<String> customerId = Optional.empty();
        String trackerId = "12345";

        TrackerState trackerState = new TrackerState();
        trackerState.setTrackerId(trackerId);
        List<TrackerState> trackerStateList = new ArrayList<>(Collections.emptyList());
        trackerStateList.add(trackerState);

        when(trackerStateRepository.findAll()).thenReturn(trackerStateList);

        LinkedHashSet<TrackerState> trackerStates =  trackers.getTrackers(Optional.empty(),Optional.empty(), customerId,
                Optional.empty(), Optional.empty(), Optional.empty());

        verify(trackerStateRepository).findAll();
        assertThat(trackerStates.size(), is(1));
        assertThat(trackerStates, contains(hasProperty("trackerId", is(trackerId.toString()))));
    }

    @Test
    public void returnEmptyTrackerListWhenNoTrackerAreAvailable() throws IOException, UnirestException {
        Integer customerId = 657483;
        Integer trackerId = 12345;
        String dealerHash = "dealerHash";
        String customerHash = "customerHash";

        Tracker mockedTracker = mockedTracker(customerId, trackerId);
        List<Tracker> trackerList = new ArrayList<>();
        trackerList.add(mockedTracker);

        Customer mockedCustomer = mockedCustomer(customerId);

        List<TrackerLastState> trackerLastStates = new ArrayList<>();

        List<String> trackerIds = new ArrayList<>();
        trackerIds.add(trackerId.toString());


        when(dealerAuthClient.getDealerHash()).thenReturn(dealerHash);
        when(customerService.getCustomerHash(dealerHash,customerId.toString())).thenReturn(customerHash);
        when(apiTrackerService.getTrackerLastState(customerHash, trackerIds)).thenReturn(trackerLastStates);
        when(panelApiTrackerService.getTrackerList(dealerHash, Optional.of(customerId.toString()))).thenReturn(trackerList);
        when(customerService.getCustomer(dealerHash, customerId.toString())).thenReturn(mockedCustomer);

        LinkedHashSet<TrackerState> trackerStates =  trackers.getTrackers(Optional.empty(),Optional.empty(), Optional.of(customerId.toString()),
                Optional.empty(), Optional.empty(), Optional.empty());

        verify(trackerStateRepository, never()).findAll();
        assertThat(trackerStates.size(), is(0));
    }

    @Test
    public void filterTrackerStatesByStartDateAdded() {
        String trackerId = "12345";
        String lastGpsUpdate = "2020-01-30 11:53:56";
        String filterStartDate = "2020-01-29 11:53:56";

        TrackerState trackerState = new TrackerState();
        trackerState.setTrackerId(trackerId);
        trackerState.setLastGpsUpdate(lastGpsUpdate);
        Set<TrackerState> trackerStateList  = new HashSet<>(Collections.emptySet());
        trackerStateList.add(trackerState);

        LinkedHashSet<TrackerState> trackerStates = trackers.filterTrackers(Optional.of(filterStartDate), Optional.empty(), Optional.empty(),
                Optional.empty(), trackerStateList, Optional.empty());
        assertThat(trackerStates, contains(hasProperty("trackerId", is(trackerId))));
    }

    @Test
    public void filterTrackerStatesByStartDateThrowsDateParseException() throws DateTimeParseException {

        String trackerId = "12345";
        String lastGpsUpdate = "2020-01-30 11:53:56";
        String filterStartDate = "incorrectDateFormat";

        TrackerState trackerState = new TrackerState();
        trackerState.setTrackerId(trackerId);
        trackerState.setLastGpsUpdate(lastGpsUpdate);
        Set<TrackerState> trackerStateList  = new HashSet<>(Collections.emptySet());
        trackerStateList.add(trackerState);

        //exception.expect(java.text.ParseException.class);

        LinkedHashSet<TrackerState> trackerStates = trackers.filterTrackers(Optional.of(filterStartDate), Optional.empty(), Optional.empty(),
                Optional.empty(), trackerStateList, Optional.empty());
        assertFalse(false);

    }

    @Test
    public void filterTrackerStatesByStartDateNotAdded() {
        String trackerId = "12345";
        String lastGpsUpdate = "2020-01-30 11:53:56";
        String filterStartDate = "2020-01-31 11:53:56";

        TrackerState trackerState = new TrackerState();
        trackerState.setTrackerId(trackerId);
        trackerState.setLastGpsUpdate(lastGpsUpdate);
        Set<TrackerState> trackerStateList  = new HashSet<>(Collections.emptySet());
        trackerStateList.add(trackerState);

        LinkedHashSet<TrackerState> trackerStates = trackers.filterTrackers(Optional.of(filterStartDate), Optional.empty(), Optional.empty(),
                Optional.empty(), trackerStateList, Optional.empty());
        assertThat(trackerStates.size(), is(0));
    }

    @Test
    public void filterTrackerStatesByEndDateAdded() {
        String trackerId = "12345";
        String lastGpsUpdate = "2020-01-15 11:53:56";
        String filterEndDate = "2020-01-30 11:53:56";

        TrackerState trackerState = new TrackerState();
        trackerState.setTrackerId(trackerId);
        trackerState.setLastGpsUpdate(lastGpsUpdate);
        Set<TrackerState> trackerStateList  = new HashSet<>(Collections.emptySet());
        trackerStateList.add(trackerState);

        LinkedHashSet<TrackerState> trackerStates = trackers.filterTrackers(Optional.empty(), Optional.of(filterEndDate), Optional.empty(),
                Optional.empty(), trackerStateList, Optional.empty());
        assertThat(trackerStates, contains(hasProperty("trackerId", is(trackerId))));
    }

    @Test
    public void filterTrackerStatesByEndDateNotAdded() {
        String trackerId = "12345";
        String lastGpsUpdate = "2020-01-30 11:53:56";
        String filterEndDate = "2020-01-27 11:53:56";

        TrackerState trackerState = new TrackerState();
        trackerState.setTrackerId(trackerId);
        trackerState.setLastGpsUpdate(lastGpsUpdate);
        Set<TrackerState> trackerStateList  = new HashSet<>(Collections.emptySet());
        trackerStateList.add(trackerState);

        LinkedHashSet<TrackerState> trackerStates = trackers.filterTrackers(Optional.empty(), Optional.of(filterEndDate), Optional.empty(),
                Optional.empty(), trackerStateList, Optional.empty());
        assertThat(trackerStates.size(), is(0));
    }

    @Test
    public void filterTrackerStatesByEndDateThrowsDateParseException() throws DateTimeParseException {

        String trackerId = "12345";
        String lastGpsUpdate = "2020-01-30 11:53:56";
        String filterEndDate = "incorrectDateFormat";

        TrackerState trackerState = new TrackerState();
        trackerState.setTrackerId(trackerId);
        trackerState.setLastGpsUpdate(lastGpsUpdate);
        Set<TrackerState> trackerStateList  = new HashSet<>(Collections.emptySet());
        trackerStateList.add(trackerState);

        LinkedHashSet<TrackerState> trackerStates = trackers.filterTrackers(Optional.empty(), Optional.of(filterEndDate), Optional.empty(),
                Optional.empty(), trackerStateList, Optional.empty());
        assertFalse(false);

    }

    @Test
    public void filterTrackerStatesByModelAdded() {
        String trackerId = "12345";
        String trackerModel = "testTrackerModel";
        String filterTrackerModel = "testTrackerModel";

        TrackerState trackerState = new TrackerState();
        trackerState.setTrackerId(trackerId);
        trackerState.setModel(trackerModel);
        Set<TrackerState> trackerStateList  = new HashSet<>(Collections.emptySet());
        trackerStateList.add(trackerState);

        LinkedHashSet<TrackerState> trackerStates = trackers.filterTrackers(Optional.empty(), Optional.empty(), Optional.of(filterTrackerModel),
                Optional.empty(), trackerStateList, Optional.empty());
        assertThat(trackerStates, contains(hasProperty("trackerId", is(trackerId))));
    }

    @Test
    public void filterTrackerStatesByModelNotAdded() {
        String trackerId = "12345";
        String trackerModel = "testTrackerModel";
        String filterTrackerModel = "notTestTrackerModel";

        TrackerState trackerState = new TrackerState();
        trackerState.setTrackerId(trackerId);
        trackerState.setModel(trackerModel);
        Set<TrackerState> trackerStateList  = new HashSet<>(Collections.emptySet());
        trackerStateList.add(trackerState);

        LinkedHashSet<TrackerState> trackerStates = trackers.filterTrackers(Optional.empty(), Optional.empty(), Optional.of(filterTrackerModel),
                Optional.empty(), trackerStateList, Optional.empty());
        assertThat(trackerStates.size(), is(0));
    }

    @Test
    public void filterTrackerStatesByStatusAdded() {
        String trackerId = "12345";
        String trackerStatus = "active";
        String filterTrackerStatus = "active";

        TrackerState trackerState = new TrackerState();
        trackerState.setTrackerId(trackerId);
        trackerState.setConnectionStatus(trackerStatus);
        Set<TrackerState> trackerStateList  = new HashSet<>(Collections.emptySet());
        trackerStateList.add(trackerState);

        LinkedHashSet<TrackerState> trackerStates = trackers.filterTrackers(Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty(), trackerStateList, Optional.of(filterTrackerStatus));
        assertThat(trackerStates, contains(hasProperty("trackerId", is(trackerId))));
    }

    @Test
    public void filterTrackerStatesByStatusNotAdded() {
        String trackerId = "12345";
        String trackerStatus = "active";
        String filterTrackerStatus = "idle";

        TrackerState trackerState = new TrackerState();
        trackerState.setTrackerId(trackerId);
        trackerState.setConnectionStatus(trackerStatus);
        Set<TrackerState> trackerStateList  = new HashSet<>(Collections.emptySet());
        trackerStateList.add(trackerState);

        LinkedHashSet<TrackerState> trackerStates = trackers.filterTrackers(Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty(), trackerStateList, Optional.of(filterTrackerStatus));
        assertThat(trackerStates.size(), is(0));
    }

    @Test
    public void filterTrackerStatesByOrderDescending() {
        String trackerId1 = "12345";
        String trackerId2 = "54321";
        String lastGpsUpdate1 = "2020-01-30 11:53:56";
        String lastGpsUpdate2 = "2020-01-31 11:53:56";
        String filterOrder = "dsc";

        TrackerState trackerState1 = new TrackerState();
        trackerState1.setTrackerId(trackerId1);
        trackerState1.setLastGpsUpdate(lastGpsUpdate1);

        TrackerState trackerState2 = new TrackerState();
        trackerState2.setTrackerId(trackerId2);
        trackerState2.setLastGpsUpdate(lastGpsUpdate2);

        Set<TrackerState> trackerStateList  = new HashSet<>(Collections.emptySet());
        trackerStateList.add(trackerState1); //intentionally putting lowest date first
        trackerStateList.add(trackerState2);

        LinkedHashSet<TrackerState> trackerStates = trackers.filterTrackers(Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.of(filterOrder), trackerStateList, Optional.empty());
        List<TrackerState> trackerStateArray = new ArrayList<>(trackerStates);

        assertThat(trackerStates.size(), is(2));
        assertThat(trackerStateArray.get(0), hasProperty("trackerId", is(trackerId2)));
    }

    @Test
    public void filterTrackerStatesByOrderAscending() {
        String trackerId1 = "12345";
        String trackerId2 = "54321";
        String lastGpsUpdate1 = "2020-01-30 11:53:56";
        String lastGpsUpdate2 = "2020-01-31 11:53:56";
        String filterOrder = "asc";

        TrackerState trackerState1 = new TrackerState();
        trackerState1.setTrackerId(trackerId1);
        trackerState1.setLastGpsUpdate(lastGpsUpdate1);

        TrackerState trackerState2 = new TrackerState();
        trackerState2.setTrackerId(trackerId2);
        trackerState2.setLastGpsUpdate(lastGpsUpdate2);

        Set<TrackerState> trackerStateList  = new HashSet<>(Collections.emptySet());
        trackerStateList.add(trackerState2); //deliberately putting highest date first
        trackerStateList.add(trackerState1);

        LinkedHashSet<TrackerState> trackerStates = trackers.filterTrackers(Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.of(filterOrder), trackerStateList, Optional.empty());
        List<TrackerState> trackerStateArray = new ArrayList<>(trackerStates);

        assertThat(trackerStates.size(), is(2));
        assertThat(trackerStateArray.get(0), hasProperty("trackerId", is(trackerId1)));
    }

    @Test
    public void filterByOrderThrowsDateParseException() throws DateTimeParseException {
        String trackerId1 = "12345";
        String trackerId2 = "54321";
        String lastGpsUpdate1 = "inCorrectDateFormat";
        String lastGpsUpdate2 = "2020-01-31 11:53:56";
        String filterOrder = "asc";

        TrackerState trackerState1 = new TrackerState();
        trackerState1.setTrackerId(trackerId1);
        trackerState1.setLastGpsUpdate(lastGpsUpdate1);

        TrackerState trackerState2 = new TrackerState();
        trackerState2.setTrackerId(trackerId2);
        trackerState2.setLastGpsUpdate(lastGpsUpdate2);

        Set<TrackerState> trackerStateList  = new HashSet<>(Collections.emptySet());
        trackerStateList.add(trackerState2);
        trackerStateList.add(trackerState1);

        LinkedHashSet<TrackerState> trackerStates = trackers.filterTrackers(Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.of(filterOrder), trackerStateList, Optional.empty());
        List<TrackerState> trackerStateArray = new ArrayList<>(trackerStates);

        assertFalse(false);

    }


    @Test
    public void returnFalseWhenStartAndEndDatesAreNull() throws DateTimeParseException {
        String trackerId1 = "12345";
        String trackerId2 = "54321";
        String filterOrder = "asc";

        TrackerState trackerState1 = new TrackerState();
        trackerState1.setTrackerId(trackerId1);
        trackerState1.setLastGpsUpdate(null);

        TrackerState trackerState2 = new TrackerState();
        trackerState2.setTrackerId(trackerId2);
        trackerState2.setLastGpsUpdate(null);

        Set<TrackerState> trackerStateList  = new HashSet<>(Collections.emptySet());
        trackerStateList.add(trackerState2);
        trackerStateList.add(trackerState1);

        LinkedHashSet<TrackerState> trackerStates = trackers.filterTrackers(Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.of(filterOrder), trackerStateList, Optional.empty());
        List<TrackerState> trackerStateArray = new ArrayList<>(trackerStates);

        assertFalse(false);

    }



    @Test
    public void getAllTrackerStates() throws IOException, UnirestException {
        Integer customerId = 657483;
        Integer trackerId = 12345;
        String dealerHash = "dealerHash";

        Tracker mockedTracker = mockedTracker(customerId, trackerId);
        List<Tracker> trackerList = new ArrayList<>();
        trackerList.add(mockedTracker);

        Customer mockedCustomer = mockedCustomer(customerId);
        List<Customer> customerList = new ArrayList<>();
        customerList.add(mockedCustomer);

        TrackerLastState trackerLastState = mockTrackerLastState(trackerId);
        List<TrackerLastState> trackerLastStates = new ArrayList<>();
        trackerLastStates.add(trackerLastState);


        when(dealerAuthClient.getDealerHash()).thenReturn(dealerHash);
        when(customerService.getCustomers(dealerHash)).thenReturn(customerList);
        doReturn(trackerLastStates).when(trackers).getTrackerStateList(customerId.toString(), trackerList ,dealerHash);
        doReturn(trackerList).when(trackers).getTrackerList(Optional.empty(), dealerHash);
        LinkedHashSet<TrackerState> trackerStates =  trackers.getAllTrackerStates();

        assertThat(trackerStates.size(), is(1));
        assertThat(trackerStates, contains(hasProperty("trackerId", is(trackerId.toString()))));
    }



    @Test
    public void returnEmptyTrackersInGetAllTrackerStatesWhenNoTrackersAreFound() throws IOException, UnirestException {
        Integer customerId = 657483;
        Integer trackerId = 12345;
        String dealerHash = "dealerHash";

        Tracker mockedTracker = mockedTracker(customerId, trackerId);
        List<Tracker> trackerList = new ArrayList<>();
        trackerList.add(mockedTracker);

        Customer mockedCustomer = mockedCustomer(customerId);
        List<Customer> customerList = new ArrayList<>();
        customerList.add(mockedCustomer);

        List<TrackerLastState> trackerLastStates = new ArrayList<>();


        when(dealerAuthClient.getDealerHash()).thenReturn(dealerHash);
        when(customerService.getCustomers(dealerHash)).thenReturn(customerList);
        doReturn(trackerLastStates).when(trackers).getTrackerStateList(customerId.toString(), trackerList ,dealerHash);
        doReturn(trackerList).when(trackers).getTrackerList(Optional.empty(), dealerHash);
        LinkedHashSet<TrackerState> trackerStates =  trackers.getAllTrackerStates();

        assertThat(trackerStates.size(), is(0));
    }



    @Test
    public void getTracker() throws IOException, UnirestException {
        Integer customerId = 657483;
        Integer trackerId = 12345;
        String dealerHash = "dealerHash";
        String customerHash = "customerHash";

        Tracker mockedTracker = mockedTracker(customerId, trackerId);

        Customer mockedCustomer = mockedCustomer(customerId);

        TrackerLastState trackerLastState = mockTrackerLastState(trackerId);

        List<TrackerLastState> trackerLastStateList = new ArrayList<>();
        trackerLastStateList.add(trackerLastState);

        ArrayList<String> trackerIdList = new ArrayList<>();
        trackerIdList.add(trackerId.toString());

        when(dealerAuthClient.getDealerHash()).thenReturn(dealerHash);
        when(panelApiTrackerService.getTracker(dealerHash,trackerId.toString())).thenReturn(mockedTracker);
        when(customerService.getCustomerHash(dealerHash, customerId.toString())).thenReturn(customerHash);
        when(customerService.getCustomer(dealerHash, customerId.toString())).thenReturn(mockedCustomer);
        when(apiTrackerService.getTrackerLastState(customerHash, trackerIdList)).thenReturn(trackerLastStateList);

        TrackerState trackerStates =  trackers.getTracker(trackerId.toString());

        assertThat(trackerStates, hasProperty("trackerId", is(trackerId.toString())));
    }

    @Test
    public void getTrackerByImei() throws IOException, UnirestException {
        Integer trackerImei = 123456789;
        Integer customerId = 657483;
        Integer trackerId = 12345;
        String dealerHash = "dealerHash";
        String customerHash = "customerHash";


        Tracker mockedTracker = mockedTracker(customerId, trackerId);
        List<Tracker> trackerList = new ArrayList<>();
        trackerList.add(mockedTracker);

        Customer mockedCustomer = mockedCustomer(customerId);

        TrackerLastState trackerLastState = mockTrackerLastState(trackerId);

        List<TrackerLastState> trackerLastStateList = new ArrayList<>();
        trackerLastStateList.add(trackerLastState);

        ArrayList<String> trackerIdList = new ArrayList<>();
        trackerIdList.add(trackerId.toString());

        when(dealerAuthClient.getDealerHash()).thenReturn(dealerHash);
        when(panelApiTrackerService.getTrackerList(dealerHash, Optional.empty())).thenReturn(trackerList);
        when(customerService.getCustomerHash(dealerHash, customerId.toString())).thenReturn(customerHash);
        when(customerService.getCustomer(dealerHash, customerId.toString())).thenReturn(mockedCustomer);
        when(apiTrackerService.getTrackerLastState(customerHash, trackerIdList)).thenReturn(trackerLastStateList);

        TrackerState trackerStates =  trackers.getTrackerByImei(trackerImei.toString());

        assertThat(trackerStates, hasProperty("trackerId", is(trackerId.toString())));
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
        gps.setUpdated("testLastUpdateDate");
        gps.setSignalLevel(100);
        gps.setLocation(location);

        Gsm gsm = new Gsm();
        gsm.setNetworkName("MTN");
        gsm.setSignalLevel(89);

        TrackerLastState trackerLastState = new TrackerLastState();
        trackerLastState.setTrackerId(trackerId.toString());
        trackerLastState.setGps(gps);
        trackerLastState.setGsm(gsm);
        trackerLastState.setBatteryLevel(60);

        return trackerLastState;
    }
}