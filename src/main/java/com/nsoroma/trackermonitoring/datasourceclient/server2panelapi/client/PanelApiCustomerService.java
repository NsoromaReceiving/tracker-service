package com.nsoroma.trackermonitoring.datasourceclient.server2panelapi.client;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.nsoroma.trackermonitoring.datasourceclient.server2panelapi.model.Customer;

import java.io.IOException;
import java.util.List;

public interface PanelApiCustomerService {

    List<Customer> getCustomers(String hash) throws IOException, UnirestException;

    String getCustomerHash(String hash, String customerId) throws IOException, UnirestException;

    Customer getCustomer(String hash, String customerId) throws IOException, UnirestException;

}
