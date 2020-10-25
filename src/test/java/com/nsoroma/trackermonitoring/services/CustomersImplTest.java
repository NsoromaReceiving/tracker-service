package com.nsoroma.trackermonitoring.services;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.nsoroma.trackermonitoring.datasourceclient.server1api.client.UnitManagerImpl;
import com.nsoroma.trackermonitoring.datasourceclient.server1api.model.Unit;
import com.nsoroma.trackermonitoring.datasourceclient.server2panelapi.client.PanelApiAuthentication;
import com.nsoroma.trackermonitoring.datasourceclient.server2panelapi.client.PanelApiCustomerServiceImpl;
import com.nsoroma.trackermonitoring.datasourceclient.server2panelapi.model.Customer;
import com.nsoroma.trackermonitoring.exceptions.DataSourceClientResponseException;
import com.nsoroma.trackermonitoring.model.customer.SlimCustomer;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;

public class CustomersImplTest {


    private PanelApiAuthentication panelApiAuthentication;
    private PanelApiCustomerServiceImpl panelApiCustomerService;

    private CustomersImpl customers;
    private UnitManagerImpl unitManager;

    @Before
    public void setUp() {
        panelApiAuthentication = mock(PanelApiAuthentication.class);
        panelApiCustomerService = mock(PanelApiCustomerServiceImpl.class);
        unitManager = mock(UnitManagerImpl.class);
        customers = new CustomersImpl();
        customers.setApiAuthentication(panelApiAuthentication);
        customers.setApiCustomerService(panelApiCustomerService);
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