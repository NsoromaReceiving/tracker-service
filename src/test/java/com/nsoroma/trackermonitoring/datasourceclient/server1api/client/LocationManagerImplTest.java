package com.nsoroma.trackermonitoring.datasourceclient.server1api.client;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import com.nsoroma.trackermonitoring.datasourceclient.server1api.model.LatestLocation;
import com.nsoroma.trackermonitoring.datasourceclient.server1api.model.UserSession;
import com.nsoroma.trackermonitoring.exceptions.DataSourceClientResponseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Unirest.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "javax.management.*"})
public class LocationManagerImplTest {

    private static final String HORCRUX = "horcrux";
    private static final String DIAGONALLEY = "diagonalley";
    private static final String THR_FAT_LADY = "thrFatLady";
    @Mock
    private GetRequest getRequest;

    private LocationManagerImpl locationManager;
    private ApiAuthenticationImpl apiAuthentication;

    @Mock
    private HttpResponse<String> httpResponse;

    @Before
    public void setUp() throws Exception {
        PowerMockito.mockStatic(Unirest.class);
        apiAuthentication = mock(ApiAuthenticationImpl.class);
        locationManager = new LocationManagerImpl();
        locationManager.setHost(HORCRUX);
        locationManager.setApiAuthentication(apiAuthentication);
    }

    @Test
    public void getLatestLocation() throws DataSourceClientResponseException, UnirestException, IOException {
        String xml = "<ArrayOfLocation xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://webservices.3dtracking.net\">\n" +
                "    <Location2>\n" +
                "        <LicensePlate />\n" +
                "        <Name>GC 6846-12</Name>\n" +
                "        <IMEI>3565434851847</IMEI>\n" +
                "        <Odometer>580614.1</Odometer>\n" +
                "        <Latitude>5.64463</Latitude>\n" +
                "        <Longitude>0.0107</Longitude>\n" +
                "        <DateTime>2017-12-30T12:59:43</DateTime>\n" +
                "        <Speed>0</Speed>\n" +
                "        <SpeedMeasure>kph</SpeedMeasure>\n" +
                "        <Heading>318</Heading>\n" +
                "        <EngineStatus>stopped</EngineStatus>\n" +
                "        <LocationDescription>Shell F/S Warehouse</LocationDescription>\n" +
                "        <DriverName> </DriverName>\n" +
                "        <DriverCode />\n" +
                "        <TripId>7474719356</TripId>\n" +
                "        <Ignition>off</Ignition>\n" +
                "        <DateUtc>2017-12-30T12:59:43</DateUtc>\n" +
                "        <Address>Shell F/S Warehouse (Fishing Harbour Road, Tema, Greater Accra Region, GH) [5.64463 / 0.0107]</Address>\n" +
                "        <Auxs>\n" +
                "            <auxiliaries xmlns=\"\">\n" +
                "                <auxiliary systemname=\"panic\" systemdescription=\"Panic Button Activated\" mappingdescription=\"\" active=\"False\" />\n" +
                "                <auxiliary systemname=\"alarm\" systemdescription=\"Alarm Output, Imobiliser\" mappingdescription=\"\" active=\"False\" />\n" +
                "                <auxiliary systemname=\"externalpowerfailure\" systemdescription=\"External Power Failure\" mappingdescription=\"\" active=\"False\" />\n" +
                "                <auxiliary systemname=\"gpsantennaremoval\" systemdescription=\"GPS antenna removal\" mappingdescription=\"\" active=\"False\" />\n" +
                "                <auxiliary systemname=\"gsmjammed\" systemdescription=\"GSM Jammed\" mappingdescription=\"\" active=\"False\" />\n" +
                "                <auxiliary systemname=\"alarmzonebroken\" systemdescription=\"Alarm Zone Broken\" mappingdescription=\"\" active=\"False\" />\n" +
                "                <auxiliary systemname=\"batterylevellow\" systemdescription=\"Battery Level Low\" mappingdescription=\"\" active=\"False\" />\n" +
                "                <auxiliary systemname=\"unitroaming\" systemdescription=\"Unit is Roaming\" mappingdescription=\"\" active=\"False\" />\n" +
                "                <auxiliary systemname=\"accident\" systemdescription=\"Accident\" mappingdescription=\"\" active=\"False\" />\n" +
                "                <auxiliary systemname=\"excessiveacceleration\" systemdescription=\"Excessive Acceleration\" mappingdescription=\"\" active=\"False\" />\n" +
                "                <auxiliary systemname=\"excessivedeceleration\" systemdescription=\"Excessive Decceleration\" mappingdescription=\"\" active=\"False\" />\n" +
                "            </auxiliaries>\n" +
                "        </Auxs>\n" +
                "    </Location2>" +
                "</ArrayOfLocation>";

        UserSession userSession = new UserSession();
        userSession.setSessionId("someSessionId");
        userSession.setUserIdGuid("somerUserId");

        when(apiAuthentication.getUserSession()).thenReturn(userSession);
        when(httpResponse.getStatus()).thenReturn(200);
        when(httpResponse.getBody()).thenReturn(xml);
        when(getRequest.asString()).thenReturn(httpResponse);
        when(Unirest.get(HORCRUX + "management/LocationManager.asmx/GetLatestLocationListForUser2?UserIdGuid="+ userSession.getUserIdGuid() +"&SessionId="+ userSession.getSessionId() +"&LastDateReceivedUtc=2000-02-08T06:00:00")).thenReturn(getRequest);

        List<LatestLocation> latestLocationList = locationManager.getLatestLocation();
        assertThat(latestLocationList.size(), is(1));
        assertThat(latestLocationList, contains(hasProperty("imei", is("3565434851847"))));
    }
}