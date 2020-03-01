package com.nsoroma.trackermonitoring.datasourceclient.panelAPI.client;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.nsoroma.trackermonitoring.datasourceclient.panelAPI.model.Customer;

import java.io.IOException;
import java.util.List;

public interface PanelApiCustomerService {

    List<Customer> getCustomers(String hash) throws IOException, UnirestException;

    String getCustomerHash(String hash, String customerId) throws IOException, UnirestException;

    Customer getCustomer(String hash, String customerId) throws IOException, UnirestException;

}
