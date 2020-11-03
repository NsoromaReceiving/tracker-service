package com.nsoroma.trackermonitoring.services;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.nsoroma.trackermonitoring.exceptions.DataSourceClientResponseException;
import com.nsoroma.trackermonitoring.model.customer.SlimCustomer;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface Customers {

   List<SlimCustomer> getCustomers() throws IOException, UnirestException, DataSourceClientResponseException;

   void saveCustomers() throws IOException, UnirestException, DataSourceClientResponseException;
}
