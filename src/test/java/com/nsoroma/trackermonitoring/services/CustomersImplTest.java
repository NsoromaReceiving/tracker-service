package com.nsoroma.trackermonitoring.services;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.nsoroma.trackermonitoring.datasourceclient.panelAPI.client.PanelApiAuthentication;
import com.nsoroma.trackermonitoring.datasourceclient.panelAPI.client.PanelApiCustomerServiceImpl;
import com.nsoroma.trackermonitoring.datasourceclient.panelAPI.model.Customer;
import com.nsoroma.trackermonitoring.model.customer.SlimCustomer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.Mockito.*;

public class CustomersImplTest {


    private PanelApiAuthentication panelApiAuthentication;
    private PanelApiCustomerServiceImpl panelApiCustomerService;
    CustomersImpl customers;
    private Customer customer;

    @Before
    public void setUp() {
        panelApiAuthentication = mock(PanelApiAuthentication.class);
        panelApiCustomerService = mock(PanelApiCustomerServiceImpl.class);
        customers = new CustomersImpl();
        customers.setApiAuthentication(panelApiAuthentication);
        customers.setApiCustomerService(panelApiCustomerService);
        customer = new Customer();
        customer.setId(12345);
        customer.setFirstName("testFirstName");
        customer.setLastName("testLastName");
        customer.setMiddleName("testMiddleName");
        customer.setLogin("testLogin");
        customer.setDealerId(598764);
    }

    @Test
    public void trimDownCustomers() throws IOException, UnirestException {
        List<Customer> customersList = new ArrayList<>();
        customersList.add(customer);
        when(panelApiAuthentication.getDealerHash()).thenReturn("dealerHash");
        when(panelApiCustomerService.getCustomers("dealerHash")).thenReturn(customersList);
        List<SlimCustomer> slimCustomerList = customers.getCustomers();
        assertThat(slimCustomerList.size(), is(1));
        assertThat(slimCustomerList, contains(hasProperty("customerId", is("12345"))));
    }
}