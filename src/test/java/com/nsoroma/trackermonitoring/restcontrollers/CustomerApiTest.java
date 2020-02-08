package com.nsoroma.trackermonitoring.restcontrollers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.nsoroma.trackermonitoring.model.customer.SlimCustomer;
import com.nsoroma.trackermonitoring.services.Customers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class CustomerApiTest {

    @InjectMocks
    CustomerApi customerApi;

    private static ObjectMapper objectMapper;
    private static MockHttpServletRequest httpServletRequest;
    private Customers customers;

    public CustomerApiTest(CustomerApi customerApi) {
        this.customerApi = customerApi;
    }

    @Before
    public void setUp() throws Exception {
        customers = mock(Customers.class);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAllCustomers() throws IOException, UnirestException {
        httpServletRequest.removeHeader("Accept");
        httpServletRequest.addHeader("Accept", "application/json");
        List<SlimCustomer> slimCustomers = new ArrayList<>();
        when(customers.getCustomers()).thenReturn(slimCustomers);

        ResponseEntity<List<SlimCustomer>> responseEntity =  customerApi.Customers();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void getNotAcceptableWhenAcceptHeadersNotPresent() throws IOException, UnirestException {
        httpServletRequest.removeHeader("Accept");

        ResponseEntity<List<SlimCustomer>> responseEntity =  customerApi.Customers();

        assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
    }

    @Test
    public void getInternalServerErrorWhenServiceThrowsError() throws IOException, UnirestException {
        httpServletRequest.removeHeader("Accept");
        httpServletRequest.addHeader("Accept", "application/json");
        when(customers.getCustomers()).thenThrow(UnirestException.class);

        ResponseEntity<List<SlimCustomer>> responseEntity =  customerApi.Customers();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void checkObjectMapper() {
       Optional<ObjectMapper> objectMapper = customerApi.getObjectMapper();
       assertTrue(objectMapper.isPresent());
    }


    @Parameterized.Parameters
    public static Collection<Object[]> instancesToTest() {
        objectMapper = new ObjectMapper();
        httpServletRequest = new MockHttpServletRequest();
        return Collections.singletonList(
                new Object[]{new CustomerApiController(objectMapper, httpServletRequest)}
        );
    }
}