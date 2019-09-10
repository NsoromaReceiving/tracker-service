package com.nsoroma.trackermonitoring.datasourceclient.panelAPI.client;

import com.nsoroma.trackermonitoring.datasourceclient.panelAPI.model.Customer;

import java.io.IOException;
import java.util.List;

public interface CustomerService {

    List<Customer> getCustomers(String hash) throws IOException;

}
