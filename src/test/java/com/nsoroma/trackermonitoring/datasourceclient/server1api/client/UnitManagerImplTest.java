package com.nsoroma.trackermonitoring.datasourceclient.server1api.client;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import com.nsoroma.trackermonitoring.datasourceclient.server1api.model.Unit;
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
import java.util.Collections;
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
public class UnitManagerImplTest {

    private static final String HORCRUX = "horcrux";
    private static final String DIAGONALLEY = "diagonalley";
    private static final String THR_FAT_LADY = "thrFatLady";
    @Mock
    private GetRequest getRequest;

    private UnitManagerImpl unitManager;
    private ApiAuthenticationImpl apiAuthentication;

    @Mock
    private HttpResponse<String> httpResponse;

    @Before
    public void setUp() throws Exception {
        PowerMockito.mockStatic(Unirest.class);
        apiAuthentication = mock(ApiAuthenticationImpl.class);
        unitManager = new UnitManagerImpl();
        unitManager.setHost(HORCRUX);
        unitManager.setApiAuthentication(apiAuthentication);
    }

    @Test
    public void getUnits() throws DataSourceClientResponseException, UnirestException, IOException {

        String xml = "<WebserviceResult xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://webservices.3dtracking.net\">\n" +
                "    <Status>OK</Status>\n" +
                "    <ErrorCode>0</ErrorCode>\n" +
                "    <ErrorDescription />\n" +
                "    <WebserviceContent>\n" +
                "        <data xmlns=\"\">\n" +
                "            <ArrayOfUnit xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "                <Unit>\n" +
                "                    <Name> box.5255</Name>\n" +
                "                    <IMEI>3574640355454255</IMEI>\n" +
                "                    <Status>Inactive</Status>\n" +
                "                    <Uid>Inactive</Uid>\n" +
                "                    <CompanyName>Inactive</CompanyName>\n" +
                "                    <UnitType>Inactive</UnitType>\n" +
                "                    <PhoneNumber>Inactive</PhoneNumber>\n" +
                "                </Unit>" +
                "             </ArrayOfUnit> " +
                "          </data>" +
                "     </WebserviceContent>" +
                "  </WebserviceResult>";

        UserSession userSession = new UserSession();
        userSession.setSessionId("someSessionId");
        userSession.setUserIdGuid("somerUserId");

        ArrayList<String> uidStrings = new ArrayList<>();
        uidStrings.add("3574640355454255");

        when(apiAuthentication.getUserSession()).thenReturn(userSession);
        when(httpResponse.getStatus()).thenReturn(200);
        when(httpResponse.getBody()).thenReturn(xml);
        when(getRequest.asString()).thenReturn(httpResponse);
        when(Unirest.get(HORCRUX + "management/DistributorManager.asmx/UnitList?UserIdGuid="+ userSession.getUserIdGuid() +"&SessionId="+ userSession.getSessionId()+"&Units="+uidStrings.get(0)+"&OptionsJSON=")).thenReturn(getRequest);

        List<Unit> latestLocationList = unitManager.getUnits(uidStrings);
        assertThat(latestLocationList.size(), is(1));
        assertThat(latestLocationList, contains(hasProperty("imei", is("3574640355454255"))));
    }
}