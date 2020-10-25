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
<<<<<<< HEAD
    private CustomersImpl customers;
=======
    private UnitManagerImpl unitManager;
    CustomersImpl customers;
>>>>>>> feature/SERVERONEINT

    @Before
    public void setUp() {
        panelApiAuthentication = mock(PanelApiAuthentication.class);
        panelApiCustomerService = mock(PanelApiCustomerServiceImpl.class);
        unitManager = mock(UnitManagerImpl.class);
        customers = new CustomersImpl();
        customers.setApiAuthentication(panelApiAuthentication);
        customers.setApiCustomerService(panelApiCustomerService);
<<<<<<< HEAD
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
=======
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
>>>>>>> feature/SERVERONEINT
        when(panelApiAuthentication.getDealerHash()).thenReturn("dealerHash");
        when(panelApiCustomerService.getCustomers("dealerHash")).thenReturn(customersList);
        when(unitManager.getUnitsStringChunks()).thenReturn(uidStrings);
        when(unitManager.getUnits(uidStrings)).thenReturn(unitList);
        List<SlimCustomer> slimCustomerList = customers.getCustomers();
        assertThat(slimCustomerList.size(), is(2));
<<<<<<< HEAD
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
=======
>>>>>>> feature/SERVERONEINT
    }
}