package com.nsoroma.trackermonitoring.services;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.nsoroma.trackermonitoring.datasourceclient.server1api.client.LocationManager;
import com.nsoroma.trackermonitoring.datasourceclient.server1api.client.LocationManagerImpl;
import com.nsoroma.trackermonitoring.datasourceclient.server1api.client.UnitManager;
import com.nsoroma.trackermonitoring.datasourceclient.server1api.client.UnitManagerImpl;
import com.nsoroma.trackermonitoring.datasourceclient.server1api.model.LatestLocation;
import com.nsoroma.trackermonitoring.datasourceclient.server1api.model.Unit;
import com.nsoroma.trackermonitoring.datasourceclient.server2api.client.ApiTrackerServiceImpl;
import com.nsoroma.trackermonitoring.datasourceclient.server2api.model.Gps;
import com.nsoroma.trackermonitoring.datasourceclient.server2api.model.Gsm;
import com.nsoroma.trackermonitoring.datasourceclient.server2api.model.Location;
import com.nsoroma.trackermonitoring.datasourceclient.server2api.model.TrackerLastState;
import com.nsoroma.trackermonitoring.datasourceclient.server2panelapi.client.PanelApiAuthentication;
import com.nsoroma.trackermonitoring.datasourceclient.server2panelapi.client.PanelApiCustomerServiceImpl;
import com.nsoroma.trackermonitoring.datasourceclient.server2panelapi.client.PanelApiTrackerServiceImpl;
import com.nsoroma.trackermonitoring.datasourceclient.server2panelapi.model.Customer;
import com.nsoroma.trackermonitoring.datasourceclient.server2panelapi.model.Source;
import com.nsoroma.trackermonitoring.datasourceclient.server2panelapi.model.Tracker;
import com.nsoroma.trackermonitoring.exceptions.DataSourceClientResponseException;
import com.nsoroma.trackermonitoring.model.trackerstate.TrackerState;
import com.nsoroma.trackermonitoring.repository.TrackerStateRepository;
import com.nsoroma.trackermonitoring.serviceutils.TrackerStateUtilsImpl;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TrackersTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private PanelApiAuthentication dealerAuthClient;
    private PanelApiCustomerServiceImpl customerService;
    private PanelApiTrackerServiceImpl panelApiTrackerService;
    private LocationManagerImpl locationManager;
    private UnitManagerImpl unitManager;
    private ApiTrackerServiceImpl apiTrackerService;
    private TrackerStateRepository trackerStateRepository;
    private Trackers trackers;



    @Before
    public void setUp() {
        dealerAuthClient = mock(PanelApiAuthentication.class);
        customerService = mock(PanelApiCustomerServiceImpl.class);
        panelApiTrackerService = mock(PanelApiTrackerServiceImpl.class);
        locationManager = mock(LocationManagerImpl.class);
        unitManager = mock(UnitManagerImpl.class);
        apiTrackerService = mock(ApiTrackerServiceImpl.class);
        trackerStateRepository = mock(TrackerStateRepository.class);
        trackers = spy(new Trackers());
        TrackerStateUtilsImpl trackerStateUtils = new TrackerStateUtilsImpl();

        trackers.setDealerAuthClient(dealerAuthClient);
        trackers.setCustomerService(customerService);
        trackers.setPanelTrackersService(panelApiTrackerService);
        trackers.setApiTrackerService(apiTrackerService);
        trackers.setTrackerStateRepository(trackerStateRepository);
<<<<<<< HEAD
        trackers.setTrackerStateUtils(trackerStateUtils);
=======
        trackers.setLocationManager(locationManager);
        trackers.setUnitManager(unitManager);
>>>>>>> feature/SERVERONEINT


    }

    @Test
    public void getTrackersWithSpecifiedCustomerID() throws IOException, UnirestException {
        Integer customerId = 657483;
        Integer trackerId = 12345;

<<<<<<< HEAD
        List<Tracker> trackerList = Collections.singletonList(mockedTracker(customerId, trackerId));
        Customer mockedCustomer = mockedCustomer(customerId);
        List<TrackerLastState> trackerLastStates = Collections.singletonList(mockTrackerLastState(trackerId));
        List<String> trackerIds = Collections.singletonList(trackerId.toString());
=======
        TrackerState trackerState = new TrackerState();
        trackerState.setTrackerId(trackerId.toString());
        trackerState.setCustomerId(customerId.toString());
>>>>>>> feature/SERVERONEINT

        List<TrackerState> trackerStateList = new ArrayList<>();
        trackerStateList.add(trackerState);

        when(trackerStateRepository.findAll()).thenReturn(trackerStateList);

        LinkedHashSet<TrackerState> trackerStates =  trackers.getTrackers(Optional.empty(),Optional.empty(), Optional.of(customerId.toString()),
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());

<<<<<<< HEAD
        verify(trackerStateRepository, never()).findAll();
        assertTrue(EqualsBuilder.reflectionEquals(mockedTrackerState(customerId.toString(),trackerId.toString()), trackerStates.stream().findFirst().get()));
=======
        assertThat(trackerStates.size(), is(1));
        assertThat(trackerStates, contains(hasProperty("trackerId", is(trackerId.toString()))));
>>>>>>> feature/SERVERONEINT
    }



    @Test
    public void getAllTrackersWithoutACustomerId() throws IOException, UnirestException {
        String trackerId = "12345";
        String someCustomer = "someCustomer";

        when(trackerStateRepository.findAll()).thenReturn(Collections.singletonList(mockedTrackerState(someCustomer, trackerId)));

<<<<<<< HEAD
        LinkedHashSet<TrackerState> trackerStates =  trackers.getTrackers(Optional.empty(),Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty());

        verify(trackerStateRepository).findAll();
        assertTrue(EqualsBuilder.reflectionEquals(mockedTrackerState(someCustomer,trackerId), trackerStates.stream().findFirst().get()));

    }

=======
        LinkedHashSet<TrackerState> trackerStates =  trackers.getTrackers(Optional.empty(),Optional.empty(), customerId,
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());

        verify(trackerStateRepository).findAll();
        assertThat(trackerStates.size(), is(1));
        assertThat(trackerStates, contains(hasProperty("trackerId", is(trackerId.toString()))));
    }

    @Test
    public void returnEmptyTrackerListWhenNoTrackerAreAvailable() throws IOException, UnirestException {
        Integer customerId = 657483;
        Integer trackerId = 12345;

        TrackerState trackerState = new TrackerState();
        trackerState.setTrackerId(trackerId.toString());
        trackerState.setCustomerId("unknownCustomerId");

        List<TrackerState> trackerStateList = new ArrayList<>();
        trackerStateList.add(trackerState);

        when(trackerStateRepository.findAll()).thenReturn(trackerStateList);


        LinkedHashSet<TrackerState> trackerStates =  trackers.getTrackers(Optional.empty(),Optional.empty(), Optional.of(customerId.toString()),
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());

        assertThat(trackerStates.size(), is(0));
    }

    @Test
    public void trackerStatesFromServerOne() throws IOException, UnirestException, DataSourceClientResponseException {
        Unit serverOneUnit1 = mockedServer1Unit();

        LatestLocation serverOneLatestLocation = getServer1LatestLocation();

        List<Unit> unitList = new ArrayList<>();
        unitList.add(serverOneUnit1);
        List<LatestLocation> latestLocationList = new ArrayList<>();
        latestLocationList.add(serverOneLatestLocation);

        ArrayList<String> uidStrings = new ArrayList<>();
        uidStrings.add(serverOneUnit1.getImei());

        when(unitManager.getUnits(uidStrings)).thenReturn(unitList);
        when(unitManager.getLatestLocation(uidStrings)).thenReturn(latestLocationList);
        when(unitManager.getUnitsStringChunks()).thenReturn(uidStrings);

        LinkedHashSet<TrackerState> trackerStateLinkedHashSet = trackers.getServerOneTrackerStates();
        assertThat(trackerStateLinkedHashSet.size(), is(1));
        assertThat(trackerStateLinkedHashSet, contains(hasProperty("trackerId", is(serverOneUnit1.getUid()))));
    }

    @Test
    public void trackerStatesFromServerOneWithInactiveStatus() throws IOException, UnirestException, DataSourceClientResponseException {
        Unit serverOneUnit1 = mockedServer1Unit();
        serverOneUnit1.setStatus("Inactive");

        LatestLocation serverOneLatestLocation = getServer1LatestLocation();

        List<Unit> unitList = new ArrayList<>();
        unitList.add(serverOneUnit1);
        List<LatestLocation> latestLocationList = new ArrayList<>();
        latestLocationList.add(serverOneLatestLocation);

        ArrayList<String> uidStrings = new ArrayList<>();
        uidStrings.add(serverOneUnit1.getImei());

        when(unitManager.getUnits(uidStrings)).thenReturn(unitList);
        when(unitManager.getUnitsStringChunks()).thenReturn(uidStrings);
        when(locationManager.getLatestLocation()).thenReturn(latestLocationList);

        LinkedHashSet<TrackerState> trackerStateLinkedHashSet = trackers.getServerOneTrackerStates();
        assertThat(trackerStateLinkedHashSet.size(), is(1));
        assertThat(trackerStateLinkedHashSet, contains(hasProperty("trackerId", is(serverOneUnit1.getImei()))));
        assertThat(trackerStateLinkedHashSet, contains(hasProperty("connectionStatus", is("offline"))));
    }

    @Test
    public void getServerOneTrackerFromDb() {
        Integer customerId = 657483;
        Integer trackerId = 12345;

        TrackerState trackerState = new TrackerState();
        trackerState.setTrackerId(trackerId.toString());
        trackerState.setCustomerId(customerId.toString());
        trackerState.setCustomerName("Unassigned");

        when(trackerStateRepository.findById(trackerId.toString())).thenReturn(Optional.of(trackerState));
        Optional<TrackerState> trackerState1 = trackers.getServerOneTracker(trackerId.toString());

        assertThat(trackerState1.get(), hasProperty("trackerId", is(trackerId.toString())));
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
                Optional.empty(), trackerStateList, Optional.empty(), Optional.empty(), Optional.empty());
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
                Optional.empty(), trackerStateList, Optional.empty(), Optional.empty(), Optional.empty());
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
                Optional.empty(), trackerStateList, Optional.empty(), Optional.empty(), Optional.empty());
        assertThat(trackerStates.size(), is(0));
    }
>>>>>>> feature/SERVERONEINT

    @Test
    public void filterTrackerStatesAscendingOrder() {
        String trackerId = "12345";
        String filterStartDate = "2020-02-15 11:53:56";
        String filterEndDate = "2020-03-29 11:53:56";
        String filterType = "someModel";
        String filterStatus = "someConnectionStatus";
        String order = "asc";

        TrackerState trackerState1 = mockedTrackerState("someCustomer", trackerId);
        TrackerState trackerState2 = mockedTrackerState("someCustomer", "34566");
        trackerState2.setLastGsmUpdate("2020-02-28 11:53:56");
        TrackerState trackerState3 = mockedTrackerState("someOtherCustomer", "23456");
        trackerState3.setLastGsmUpdate("2020-02-24 11:53:56");
        trackerState3.setModel("someOtherModel");
        TrackerState trackerState4 = mockedTrackerState("customer", "12437");
        trackerState4.setLastGsmUpdate("2020-02-01 11:53:56");
        Set<TrackerState> trackerStateList  = new HashSet<>(Collections.emptySet());
<<<<<<< HEAD
        trackerStateList.add(trackerState3);
        trackerStateList.add(trackerState2);
        trackerStateList.add(trackerState4);
        trackerStateList.add(trackerState1);
=======
        trackerStateList.add(trackerState);

        LinkedHashSet<TrackerState> trackerStates = trackers.filterTrackers(Optional.empty(), Optional.of(filterEndDate), Optional.empty(),
                Optional.empty(), trackerStateList, Optional.empty(), Optional.empty(), Optional.empty());
        assertThat(trackerStates, contains(hasProperty("trackerId", is(trackerId))));
    }
>>>>>>> feature/SERVERONEINT

        LinkedHashSet<TrackerState> trackerStates = trackers.filterTrackers(Optional.of(filterStartDate), Optional.of(filterEndDate), Optional.of(filterType),
                Optional.of(order), trackerStateList, Optional.of(filterStatus));

<<<<<<< HEAD
        assertTrue(EqualsBuilder.reflectionEquals(trackerState1, trackerStates.stream().findFirst().get()));
=======
        TrackerState trackerState = new TrackerState();
        trackerState.setTrackerId(trackerId);
        trackerState.setLastGpsUpdate(lastGpsUpdate);
        Set<TrackerState> trackerStateList  = new HashSet<>(Collections.emptySet());
        trackerStateList.add(trackerState);

        LinkedHashSet<TrackerState> trackerStates = trackers.filterTrackers(Optional.empty(), Optional.of(filterEndDate), Optional.empty(),
                Optional.empty(), trackerStateList, Optional.empty(), Optional.empty(), Optional.empty());
        assertThat(trackerStates.size(), is(0));
>>>>>>> feature/SERVERONEINT
    }

    @Test
    public void filterTrackerStatesDescendingOrder() {
        String trackerId = "12345";
        String filterStartDate = "2020-02-15 11:53:56";
        String filterEndDate = "2020-03-29 11:53:56";
        String filterType = "someModel";
        String filterStatus = "someConnectionStatus";
        String order = "dsc";

        TrackerState trackerState1 = mockedTrackerState("someCustomer", trackerId);

<<<<<<< HEAD
        TrackerState trackerState2 = mockedTrackerState("someCustomer", "34566");
        trackerState2.setLastGsmUpdate("2020-02-17 11:53:56");
=======
        LinkedHashSet<TrackerState> trackerStates = trackers.filterTrackers(Optional.empty(), Optional.of(filterEndDate), Optional.empty(),
                Optional.empty(), trackerStateList, Optional.empty(), Optional.empty(), Optional.empty());
        assertFalse(false);
>>>>>>> feature/SERVERONEINT

        TrackerState trackerState3 = mockedTrackerState("someOtherCustomer", "23456");
        trackerState3.setConnectionStatus("someOtherConnectionStatus");
        trackerState3.setLastGsmUpdate("2020-03-28 11:53:46");

<<<<<<< HEAD
        TrackerState trackerState4 = mockedTrackerState("customer", "12427");
        trackerState4.setLastGsmUpdate("2020-04-28 11:53:46");
=======

    @Test
    public void filterTrackerStatesByServerAdded() {
        String trackerId = "12345";
        String server = "server1";

        TrackerState trackerState = new TrackerState();
        trackerState.setTrackerId(trackerId);
        trackerState.setServer(server);
        Set<TrackerState> trackerStateList  = new HashSet<>(Collections.emptySet());
        trackerStateList.add(trackerState);

        LinkedHashSet<TrackerState> trackerStates = trackers.filterTrackers(Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty(), trackerStateList, Optional.empty(), Optional.of(server),Optional.empty());
        assertThat(trackerStates, contains(hasProperty("trackerId", is(trackerId))));
    }

    @Test
    public void filterTrackerStatesByServerNotAdded() {
        String trackerId = "12345";
        String server = "server1";
        String filterServer = "server2";

        TrackerState trackerState = new TrackerState();
        trackerState.setTrackerId(trackerId);
        trackerState.setServer(server);
        Set<TrackerState> trackerStateList  = new HashSet<>(Collections.emptySet());
        trackerStateList.add(trackerState);

        LinkedHashSet<TrackerState> trackerStates = trackers.filterTrackers(Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty(), trackerStateList, Optional.empty(), Optional.of(filterServer),Optional.empty());
        assertThat(trackerStates.size(), is(0));
    }

    @Test
    public void filterTrackerStatesByModelAdded() {
        String trackerId = "12345";
        String trackerModel = "testTrackerModel";
        String filterTrackerModel = "testTrackerModel";
>>>>>>> feature/SERVERONEINT

        Set<TrackerState> trackerStateList  = new HashSet<>(Collections.emptySet());
<<<<<<< HEAD
        trackerStateList.add(trackerState4);
        trackerStateList.add(trackerState3);
        trackerStateList.add(trackerState2);
        trackerStateList.add(trackerState1);
=======
        trackerStateList.add(trackerState);

        LinkedHashSet<TrackerState> trackerStates = trackers.filterTrackers(Optional.empty(), Optional.empty(), Optional.of(filterTrackerModel),
                Optional.empty(), trackerStateList, Optional.empty(), Optional.empty(), Optional.empty());
        assertThat(trackerStates, contains(hasProperty("trackerId", is(trackerId))));
    }

    @Test
    public void filterTrackerStatesByModelNotAdded() {
        String trackerId = "12345";
        String trackerModel = "testTrackerModel";
        String filterTrackerModel = "notTestTrackerModel";
>>>>>>> feature/SERVERONEINT

        LinkedHashSet<TrackerState> trackerStates = trackers.filterTrackers(Optional.of(filterStartDate), Optional.of(filterEndDate), Optional.of(filterType),
                Optional.of(order), trackerStateList, Optional.of(filterStatus));

<<<<<<< HEAD
        assertTrue(EqualsBuilder.reflectionEquals(trackerState1, trackerStates.stream().findFirst().get()));
    }

=======
        LinkedHashSet<TrackerState> trackerStates = trackers.filterTrackers(Optional.empty(), Optional.empty(), Optional.of(filterTrackerModel),
                Optional.empty(), trackerStateList, Optional.empty(), Optional.empty(), Optional.empty());
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
                Optional.empty(), trackerStateList, Optional.of(filterTrackerStatus), Optional.empty(), Optional.empty());
        assertThat(trackerStates, contains(hasProperty("trackerId", is(trackerId))));
    }
>>>>>>> feature/SERVERONEINT

    @Test
    public void throwDateParseExceptionWhenFilteringStartDate() {
        String trackerId = "12345";
<<<<<<< HEAD
        String filterStartDate = "2020-02-15 11:53:56";
=======
        String trackerStatus = "active";
        String filterTrackerStatus = "idle";

        TrackerState trackerState = new TrackerState();
        trackerState.setTrackerId(trackerId);
        trackerState.setConnectionStatus(trackerStatus);
        Set<TrackerState> trackerStateList  = new HashSet<>(Collections.emptySet());
        trackerStateList.add(trackerState);

        LinkedHashSet<TrackerState> trackerStates = trackers.filterTrackers(Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty(), trackerStateList, Optional.of(filterTrackerStatus), Optional.empty(), Optional.empty());
        assertThat(trackerStates.size(), is(0));
    }

    @Test
    public void filterTrackerStatesByOrderDescending() {
        String trackerId1 = "12345";
        String trackerId2 = "54321";
        String lastGpsUpdate1 = "2020-01-30 11:53:56";
        String lastGpsUpdate2 = "2020-01-31 11:53:56";
        String filterOrder = "dsc";
>>>>>>> feature/SERVERONEINT

        TrackerState trackerState1 = mockedTrackerState("someCustomer", trackerId);
        TrackerState trackerState2 = mockedTrackerState("someOtherCustomer", "23456");
        trackerState2.setLastGsmUpdate("wrongDateFormat");

        Set<TrackerState> trackerStateList  = new HashSet<>(Collections.emptySet());
        trackerStateList.add(trackerState1);
        trackerStateList.add(trackerState2);

<<<<<<< HEAD
        LinkedHashSet<TrackerState> trackerStates = trackers.filterTrackers(Optional.of(filterStartDate), Optional.empty(), Optional.empty(),
                Optional.empty(), trackerStateList, Optional.empty());
=======
        LinkedHashSet<TrackerState> trackerStates = trackers.filterTrackers(Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.of(filterOrder), trackerStateList, Optional.empty(), Optional.empty(), Optional.empty());
        List<TrackerState> trackerStateArray = new ArrayList<>(trackerStates);
>>>>>>> feature/SERVERONEINT

        assertTrue(EqualsBuilder.reflectionEquals(trackerState1, trackerStates.stream().findFirst().get()));
        assertEquals(1, trackerStates.size());
    }

    @Test
    public void throwDateParseExceptionWhenFilteringEndDate() {
        String trackerId = "12345";
        String filterEndDate = "2020-04-15 11:53:56";

        TrackerState trackerState1 = mockedTrackerState("someCustomer", trackerId);
        TrackerState trackerState2 = mockedTrackerState("someOtherCustomer", "23456");
        trackerState2.setLastGsmUpdate("wrongDateFormat");

        Set<TrackerState> trackerStateList  = new HashSet<>(Collections.emptySet());
        trackerStateList.add(trackerState1);
<<<<<<< HEAD
        trackerStateList.add(trackerState2);
=======

        LinkedHashSet<TrackerState> trackerStates = trackers.filterTrackers(Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.of(filterOrder), trackerStateList, Optional.empty(), Optional.empty(), Optional.empty());
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
                Optional.of(filterOrder), trackerStateList, Optional.empty(), Optional.empty(), Optional.empty());
        List<TrackerState> trackerStateArray = new ArrayList<>(trackerStates);
>>>>>>> feature/SERVERONEINT

        LinkedHashSet<TrackerState> trackerStates = trackers.filterTrackers(Optional.empty(), Optional.of(filterEndDate), Optional.empty(),
                Optional.empty(), trackerStateList, Optional.empty());

        assertTrue(EqualsBuilder.reflectionEquals(trackerState1, trackerStates.stream().findFirst().get()));
        assertEquals(1, trackerStates.size());
    }

    @Test
    public void throwDateParseExceptionWhenOrdering() {
        String trackerId = "12345";

        TrackerState trackerState1 = mockedTrackerState("someCustomer", trackerId);
        TrackerState trackerState2 = mockedTrackerState("someOtherCustomer", "23456");
        trackerState2.setLastGsmUpdate("2020-08-15 11:53:56");
        TrackerState trackerState3 = mockedTrackerState("otherCustomer", "13434");
        trackerState3.setLastGsmUpdate("wrongDateFormat");

        Set<TrackerState> trackerStateList  = new HashSet<>(Collections.emptySet());
        trackerStateList.add(trackerState1);
        trackerStateList.add(trackerState2);
        trackerStateList.add(trackerState3);

        LinkedHashSet<TrackerState> trackerStates = trackers.filterTrackers(Optional.empty(), Optional.empty(), Optional.empty(),
<<<<<<< HEAD
                Optional.of("dsc"), trackerStateList, Optional.empty());
        assertEquals(3, trackerStates.size());
=======
                Optional.of(filterOrder), trackerStateList, Optional.empty(), Optional.empty(), Optional.empty());
        List<TrackerState> trackerStateArray = new ArrayList<>(trackerStates);

        assertFalse(false);

>>>>>>> feature/SERVERONEINT
    }


    @Test
    public void getAllTrackerStates() throws IOException, UnirestException {
        Integer customerId = 657483;
        Integer trackerId = 12345;
        String dealerHash = "dealerHash";
<<<<<<< HEAD
        String customerHash = "customerHash";

        Tracker tracker1 = mockedTracker(customerId, trackerId);
        Tracker tracker2 = mockedTracker(134566, 212133);
=======
        List<String> trackerIds = new ArrayList<>();
        trackerIds.add(trackerId.toString());

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
        //doReturn(trackerList).when(trackers).getTrackerList(Optional.empty(), dealerHash);
        when(panelApiTrackerService.getTrackerList(dealerHash, Optional.empty())).thenReturn(trackerList);
        when(customerService.getCustomerHash(dealerHash,customerId.toString())).thenReturn("customerHash");
        when(apiTrackerService.getTrackerLastState("customerHash", trackerIds)).thenReturn(trackerLastStates);

        LinkedHashSet<TrackerState> trackerStates =  trackers.getServerTwoTrackerStates();

        assertThat(trackerStates.size(), is(1));
        assertThat(trackerStates, contains(hasProperty("trackerId", is(trackerId.toString()))));
    }



    @Test
    public void returnEmptyTrackersInGetAllTrackerStatesWhenNoTrackersAreFound() throws IOException, UnirestException {
        Integer customerId = 657483;
        Integer trackerId = 12345;
        String dealerHash = "dealerHash";
>>>>>>> feature/SERVERONEINT

        List<Tracker> trackerList = new ArrayList<>();
        trackerList.add(tracker2);
        trackerList.add(tracker1);

        List<Customer> customerList = Collections.singletonList(mockedCustomer(customerId));
        List<String> trackerIds = Collections.singletonList(trackerId.toString());
        List<TrackerLastState> trackerLastStates = Collections.singletonList(mockTrackerLastState(trackerId));

        when(dealerAuthClient.getDealerHash()).thenReturn(dealerHash);
        when(customerService.getCustomers(dealerHash)).thenReturn(customerList);
<<<<<<< HEAD
        when(panelApiTrackerService.getTrackerList(dealerHash, Optional.empty())).thenReturn(trackerList);
        when(panelApiTrackerService.getTrackerList(dealerHash, Optional.of(customerId.toString()))).thenReturn(trackerList);
        when(customerService.getCustomerHash(dealerHash,customerId.toString())).thenReturn(customerHash);
        when(apiTrackerService.getTrackerLastState(customerHash, trackerIds)).thenReturn(trackerLastStates);

        LinkedHashSet<TrackerState> trackerStates =  trackers.getAllTrackerStates();
=======
        doReturn(trackerLastStates).when(trackers).getTrackerStateList(customerId.toString(), trackerList ,dealerHash);
        doReturn(trackerList).when(trackers).getTrackerList(Optional.empty(), dealerHash);
        LinkedHashSet<TrackerState> trackerStates =  trackers.getServerTwoTrackerStates();
>>>>>>> feature/SERVERONEINT

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

        TrackerState trackerStates =  trackers.getServerTwoTracker(trackerId.toString());

        assertTrue(EqualsBuilder.reflectionEquals(mockedTrackerState(customerId.toString(), trackerId.toString()), trackerStates));
    }

    @Test
    public void getTrackerByImei() throws IOException, UnirestException {
        Integer trackerImei = 123456789;
        Integer customerId = 657483;
        Integer trackerId = 12345;
        String dealerHash = "dealerHash";
        String customerHash = "customerHash";

        Tracker tracker1 = mockedTracker(customerId, trackerId);
        tracker1.getSource().setDeviceId(trackerImei.toString());

        Tracker tracker2 = mockedTracker(customerId, 22234);
        tracker2.getSource().setDeviceId("45345365");

        List<Tracker> trackerList = new ArrayList<>();
        trackerList.add(tracker2);
        trackerList.add(tracker1);

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

<<<<<<< HEAD
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

=======
    private Unit mockedServer1Unit() {
        Unit serverOneUnit = new Unit();
        serverOneUnit.setName("unit1");
        serverOneUnit.setImei("109193090130");
        serverOneUnit.setStatus("Active");
        serverOneUnit.setGroupName("Unassigned");
        serverOneUnit.setCompany("someCompany");
        serverOneUnit.setPhoneNumber("somePhoneNumber");
        serverOneUnit.setUid("someUid");
        serverOneUnit.setUnitType("someUnitType");
        return serverOneUnit;
    }

    private LatestLocation getServer1LatestLocation() {
        LatestLocation serverOneLatestLocation = new LatestLocation();
        serverOneLatestLocation.setImei("109193090130");
        serverOneLatestLocation.setName("unit1");
        serverOneLatestLocation.setDateTime("2020-01-29T11:53:56");
        serverOneLatestLocation.setLatitude("-1.24343");
        serverOneLatestLocation.setLongitude("0.5321");
        return serverOneLatestLocation;
>>>>>>> feature/SERVERONEINT
    }
}