package com.nsoroma.trackermonitoring.datasourceclient.panelAPI.client;

import com.nsoroma.trackermonitoring.datasourceclient.panelAPI.model.Customer;

import java.io.IOException;
import java.util.List;

public interface PanelApiCustomerService {

    List<Customer> getCustomers(String hash) throws IOException;

    String getCustomerHash(String hash, String customerId) throws IOException;

    Customer getCustomer(String hash, String customerId) throws IOException;

}
