package com.nsoroma.trackermonitoring.services;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.nsoroma.trackermonitoring.datasourceclient.server1api.client.UnitManagerImpl;
import com.nsoroma.trackermonitoring.datasourceclient.server1api.model.Unit;
import com.nsoroma.trackermonitoring.datasourceclient.server2panelapi.client.PanelApiAuthentication;
import com.nsoroma.trackermonitoring.datasourceclient.server2panelapi.client.PanelApiCustomerServiceImpl;
import com.nsoroma.trackermonitoring.datasourceclient.server2panelapi.model.Customer;
import com.nsoroma.trackermonitoring.exceptions.DataSourceClientResponseException;
import com.nsoroma.trackermonitoring.model.customer.SlimCustomer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.Mockito.*;

public class CustomersImplTest {


    private PanelApiAuthentication panelApiAuthentication;
    private PanelApiCustomerServiceImpl panelApiCustomerService;
    private UnitManagerImpl unitManager;
    CustomersImpl customers;

    @Before
    public void setUp() {
        panelApiAuthentication = mock(PanelApiAuthentication.class);
        panelApiCustomerService = mock(PanelApiCustomerServiceImpl.class);
        unitManager = mock(UnitManagerImpl.class);
        customers = new CustomersImpl();
        customers.setApiAuthentication(panelApiAuthentication);
        customers.setApiCustomerService(panelApiCustomerService);
        customers.setUnitManager(unitManager);
    }

    @Test
    public void trimDownCustomers() throws IOException, UnirestException, DataSourceClientResponseException {
        Customer customer = new Customer();
        customer.setId(12345);
        customer.setFirstName("testFirstName");
        customer.setLastName("testLastName");
        customer.setMiddleName("testMiddleName");
        customer.setLogin("testLogin");
        customer.setDealerId(598764);

        Unit unit = new Unit();
        unit.setImei("1234567789");
        unit.setGroupName("Unassigned");
        unit.setCompany("someCompany");

        List<Customer> customersList = new ArrayList<>();
        customersList.add(customer);

        ArrayList<String> uidStrings = new ArrayList<>();
        uidStrings.add("1234567789");

        List<Unit> unitList = new ArrayList<>();
        unitList.add(unit);
        when(panelApiAuthentication.getDealerHash()).thenReturn("dealerHash");
        when(panelApiCustomerService.getCustomers("dealerHash")).thenReturn(customersList);
        when(unitManager.getUnits(uidStrings)).thenReturn(unitList);
        List<SlimCustomer> slimCustomerList = customers.getCustomers();
        assertThat(slimCustomerList.size(), is(2));
    }
}