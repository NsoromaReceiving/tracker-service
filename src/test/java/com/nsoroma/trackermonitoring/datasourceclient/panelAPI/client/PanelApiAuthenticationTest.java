package com.nsoroma.trackermonitoring.datasourceclient.panelAPI.client;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*"})
@PrepareForTest({Unirest.class})
public class PanelApiAuthenticationTest {

    private PanelApiAuthentication panelApiAuthentication;

    @Mock
    private GetRequest getRequest;

    @Mock
    private HttpResponse<JsonNode> httpResponse;

    @Before
    public void setUp() throws Exception {
        PowerMockito.mockStatic(Unirest.class);
        panelApiAuthentication = new PanelApiAuthentication();
        panelApiAuthentication.setHost("testHost/");
        panelApiAuthentication.setUsername("testUsername");
        panelApiAuthentication.setPassword("testPassword");
    }

    @Test
    public void getDealerAuthenticationHash() throws UnirestException {
        JsonNode json = new JsonNode("{\"hash\":dealerHash}");
        when(httpResponse.getStatus()).thenReturn(200);
        when(httpResponse.getBody()).thenReturn(json);
        when(Unirest.get("testHost/account/auth?login=testUsername&password=testPassword")).thenReturn(getRequest);
        when(getRequest.header("accept", "application/json")).thenReturn(getRequest);
        when(getRequest.asJson()).thenReturn(httpResponse);
        String hash = panelApiAuthentication.getDealerHash();
        assertEquals("dealerHash", hash);
    }

    @Test
    public void doNotReadResponseWhenServerStatusIsNot200() throws UnirestException {
        JsonNode json = new JsonNode("{\"hash\":dealerHash}");
        when(httpResponse.getStatus()).thenReturn(400);
        when(Unirest.get("testHost/account/auth?login=testUsername&password=testPassword")).thenReturn(getRequest);
        when(getRequest.header("accept", "application/json")).thenReturn(getRequest);
        when(getRequest.asJson()).thenReturn(httpResponse);
        String hash = panelApiAuthentication.getDealerHash();
        verify(httpResponse, never()).getBody();
    }

   @Test
    public void checkAuthenticationUrlConstruction() {
        String authenticationUrl = panelApiAuthentication.constructAuthenticationUrl();
        assertEquals("testHost/account/auth?login=testUsername&password=testPassword", authenticationUrl);
    }
}
