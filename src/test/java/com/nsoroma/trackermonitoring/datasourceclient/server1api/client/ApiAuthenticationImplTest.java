package com.nsoroma.trackermonitoring.datasourceclient.server1api.client;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
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

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Unirest.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "javax.management.*"})
public class ApiAuthenticationImplTest {

    private static final String HORCRUX = "horcrux";
    private static final String DIAGONALLEY = "diagonalley";
    private static final String THR_FAT_LADY = "thrFatLady";
    @Mock
    private GetRequest getRequest;

    private ApiAuthenticationImpl apiAuthentication;

    @Mock
    private HttpResponse<String> httpResponse;

    @Before
    public void setUp() throws Exception {
        PowerMockito.mockStatic(Unirest.class);
        apiAuthentication = new ApiAuthenticationImpl();
        apiAuthentication.setHost(HORCRUX);
        apiAuthentication.setPassword(DIAGONALLEY);
        apiAuthentication.setUsername(THR_FAT_LADY);
    }

    @Test
    public void getUserSession() throws UnirestException, IOException, DataSourceClientResponseException {
        String xml = "<UserSessionToken xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns=\"http://webservices.3dtracking.net\">\n" +
                "<WebserviceUri>https://webservicesaf2.3dtracking.net</WebserviceUri>\n" +
                "<UserIdGuid>5f2faf8e-e0fb-43bd-8c53-0c8756526f5f</UserIdGuid>\n" +
                "<SessionId>42471050-7c4b-4944-9a07-879e64363546</SessionId>\n" +
                "<Uri>https://tracking.nsoromagps.com</Uri>\n" +
                "</UserSessionToken>";
        when(httpResponse.getStatus()).thenReturn(200);
        when(httpResponse.getBody()).thenReturn(xml);
        when(getRequest.asString()).thenReturn(httpResponse);
        when(Unirest.get(HORCRUX + "management/Authenticationmanager.asmx/UserAuthenticate?UserName=" + THR_FAT_LADY + "&Password=" + DIAGONALLEY)).thenReturn(getRequest);

        UserSession userSession = apiAuthentication.getUserSession();
        assertEquals("42471050-7c4b-4944-9a07-879e64363546", userSession.getSessionId());
    }
}