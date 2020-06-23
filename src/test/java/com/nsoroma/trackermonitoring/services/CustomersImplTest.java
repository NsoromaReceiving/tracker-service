package com.nsoroma.trackermonitoring.services;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.nsoroma.trackermonitoring.datasourceclient.panelAPI.client.PanelApiAuthentication;
import com.nsoroma.trackermonitoring.datasourceclient.panelAPI.client.PanelApiCustomerServiceImpl;
import com.nsoroma.trackermonitoring.datasourceclient.panelAPI.model.Customer;
import com.nsoroma.trackermonitoring.model.customer.SlimCustomer;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;

public class CustomersImplTest {


    private PanelApiAuthentication panelApiAuthentication;
    private PanelApiCustomerServiceImpl panelApiCustomerService;
    private CustomersImpl customers;

    @Before
    public void setUp() {
        panelApiAuthentication = mock(PanelApiAuthentication.class);
        panelApiCustomerService = mock(PanelApiCustomerServiceImpl.class);
        customers = new CustomersImpl();
        customers.setApiAuthentication(panelApiAuthentication);
        customers.setApiCustomerService(panelApiCustomerService);
    }

    @Test
    public void trimDownCustomers() throws IOException, UnirestException {
        SlimCustomer customerSlim = new SlimCustomer();
        customerSlim.setLogin("testLogin");
        customerSlim.setCustomerName("testFirstName testMiddleName testLastName");
        customerSlim.setCustomerId("243343");

        Customer customer1 = mockedCustomer(243343);
        Customer customer2 = mockedCustomer(242111);
        customer2.setFirstName("someFirstName");

        List<Customer> customersList = new ArrayList<>();
        customersList.add(customer1);
        customersList.add(customer2);
        when(panelApiAuthentication.getDealerHash()).thenReturn("dealerHash");
        when(panelApiCustomerService.getCustomers("dealerHash")).thenReturn(customersList);
        List<SlimCustomer> slimCustomerList = customers.getCustomers();
        assertThat(slimCustomerList.size(), is(2));
        assertTrue(EqualsBuilder.reflectionEquals(customerSlim, slimCustomerList.get(1)));
    }

    private Customer mockedCustomer(int customerId) {
        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setFirstName("testFirstName");
        customer.setLastName("testLastName");
        customer.setMiddleName("testMiddleName");
        customer.setLogin("testLogin");
        customer.setDealerId(598764);

        return customer;
    }
}