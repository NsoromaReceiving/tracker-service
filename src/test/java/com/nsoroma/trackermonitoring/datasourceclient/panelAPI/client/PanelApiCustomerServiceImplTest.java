package com.nsoroma.trackermonitoring.datasourceclient.panelAPI.client;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import com.nsoroma.trackermonitoring.datasourceclient.panelAPI.model.Customer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Unirest.class})
public class PanelApiCustomerServiceImplTest {

    private PanelApiCustomerServiceImpl panelApiCustomerService;

    @Mock
    private GetRequest getRequest;
    @Mock
    private HttpResponse<JsonNode> httpResponse;
    private String host = "testHost/";
    private String dealerHash = "dealerHash";


    @Before
    public void setUp() {
        PowerMockito.mockStatic(Unirest.class);
        panelApiCustomerService = new PanelApiCustomerServiceImpl();
        panelApiCustomerService.setHost(host);
    }


    @Test
    public void getAllCustomers() throws IOException, UnirestException {

        JsonNode json = new JsonNode("{\"list\":[{\"id\":123456789, \"dealer_id\":987654321, \"first_name\":testFirstName, " +
                "\"middle_name\":testMiddleName, \"last_name\": testLastName, \"phone\":testPhone}]}");

        when(httpResponse.getStatus()).thenReturn(200);
        when(httpResponse.getBody()).thenReturn(json);
        when(Unirest.get(host + "user/list/?hash=" + dealerHash)).thenReturn(getRequest);
        when(getRequest.header("accept", "application/json")).thenReturn(getRequest);
        when(getRequest.asJson()).thenReturn(httpResponse);

        List<Customer> customers = panelApiCustomerService.getCustomers(dealerHash);
        assertThat(customers.size(), is(1));
        assertThat(customers, contains(hasProperty("id", is(123456789))));

    }

    @Test
    public void getHashCodeForACustomer() throws IOException, UnirestException {
        String customerId = "customerId";
        JsonNode json = new JsonNode("{\"hash\":customerHash}");
        when(httpResponse.getStatus()).thenReturn(200);
        when(httpResponse.getBody()).thenReturn(json);
        when(Unirest.get(host + "user/session/create/?user_id=" + customerId + "&hash=" + dealerHash)).thenReturn(getRequest);
        when(getRequest.header("accept", "application/json")).thenReturn(getRequest);
        when(getRequest.asJson()).thenReturn(httpResponse);
        String customerHash = panelApiCustomerService.getCustomerHash(dealerHash, customerId);
        assertEquals("customerHash", customerHash);
    }

    @Test
    public void getDetailsOfACustomer() throws UnirestException, IOException {
        String customerId = "customerId";
        JsonNode json = new JsonNode("{\"value\":{\"id\":123456789, \"dealer_id\":987654321, \"first_name\":testFirstName, " +
                "\"middle_name\":testMiddleName, \"last_name\": testLastName, \"phone\":testPhone}}");

        when(httpResponse.getStatus()).thenReturn(200);
        when(httpResponse.getBody()).thenReturn(json);
        when(Unirest.get(host + "user/read/?user_id="+ customerId +"&hash=" + dealerHash)).thenReturn(getRequest);
        when(getRequest.header("accept", "application/json")).thenReturn(getRequest);
        when(getRequest.asJson()).thenReturn(httpResponse);

        Customer customer = panelApiCustomerService.getCustomer(dealerHash, customerId);
        assertThat(customer, hasProperty("id", is(123456789)));
    }

    @Test
    public void checkGetCustomersUrlConstruction() {
        String customersUrl = panelApiCustomerService.constructCustomersUrl(dealerHash);
        assertEquals("wrong customers url in panel api customer service",host +"user/list/?hash=" + dealerHash, customersUrl);
    }

    @Test
    public void checkGetCustomerUrlConstruction() {
        String customerId = "customerId";
        String testUrl = "test/Customer/Resource/Url";
        String customerUrl = panelApiCustomerService.constructCustomerUrl(dealerHash, customerId, testUrl);
        assertEquals("wrong url construction for customer in panel service", host + testUrl + customerId + "&hash=" + dealerHash, customerUrl);

    }

    @Test
    public void returnEmptyCustomerHashWhenNot200() throws UnirestException {
        String customerId = "customerId";
        when(httpResponse.getStatus()).thenReturn(400);
        when(Unirest.get(host + "user/session/create/?user_id=" + customerId + "&hash=" + dealerHash)).thenReturn(getRequest);
        when(getRequest.header("accept", "application/json")).thenReturn(getRequest);
        when(getRequest.asJson()).thenReturn(httpResponse);

        String customerHash = panelApiCustomerService.getCustomerHash(dealerHash, customerId);
        verify(httpResponse, never()).getBody();
        assertEquals("", customerHash);
    }

    @Test
    public void returnNullCustomerWhenStatusIsNot200() throws UnirestException, IOException {
        String customerId = "customerId";

        when(httpResponse.getStatus()).thenReturn(500);
        when(Unirest.get(host + "user/read/?user_id="+ customerId +"&hash=" + dealerHash)).thenReturn(getRequest);
        when(getRequest.header("accept", "application/json")).thenReturn(getRequest);
        when(getRequest.asJson()).thenReturn(httpResponse);

        Customer customer = panelApiCustomerService.getCustomer(dealerHash, customerId);
        verify(httpResponse, never()).getBody();
    }
}