package com.nsoroma.trackermonitoring.datasourceclient.panelAPI.client;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.nsoroma.trackermonitoring.datasourceclient.panelAPI.model.Customer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class PanelApiCustomerServiceImpl implements PanelApiCustomerService {

    private static final String ACCEPT = "accept";
    private static final String APPLICATION_JSON = "application/json";
    @Value("${nsoromagps.server2.panelAPI.host}")
    private
    String host;

    @Override
    public List<Customer> getCustomers(String hash) throws IOException, UnirestException {
        String customerUrl = constructCustomersUrl(hash);
        List<Customer> customerList = new ArrayList<>();

        HttpResponse<JsonNode> customerResponse = Unirest.get(customerUrl).header(ACCEPT, APPLICATION_JSON).asJson();
        if(customerResponse.getStatus() == 200) {
            ObjectMapper objectMapper = new ObjectMapper();
            String customerObjectString = customerResponse.getBody().getObject().getJSONArray("list").toString();
            customerList = Arrays.asList(objectMapper.readValue(customerObjectString, Customer[].class));
        }
        return customerList;
    }

    @Override
    public String getCustomerHash(String hash, String customerId) throws UnirestException {
        String customerSessionUrl = "user/session/create/?user_id=";
        String customerSessionURL = constructCustomerUrl(hash, customerId, customerSessionUrl);
        String customerHash = "";

        HttpResponse<JsonNode> customerSessionResponse = Unirest.get(customerSessionURL).header(ACCEPT, APPLICATION_JSON).asJson();
        if(customerSessionResponse.getStatus() == 200) {
            customerHash = customerSessionResponse.getBody().getObject().getString("hash");
        }
        return customerHash;
    }

    @Override
    public Customer getCustomer(String hash, String customerId) throws IOException, UnirestException {
        String customerUrl = "user/read/?user_id=";
        String customerURL = constructCustomerUrl(hash, customerId, customerUrl);
        Customer customer = new Customer();

        HttpResponse<JsonNode> customerResponse = Unirest.get(customerURL).header(ACCEPT, APPLICATION_JSON).asJson();
        if(customerResponse.getStatus() == 200) {
            ObjectMapper objectMapper = new ObjectMapper();
            String customerObjectString = customerResponse.getBody().getObject().getJSONObject("value").toString();
            customer = objectMapper.readValue(customerObjectString, Customer.class);
        }

        return customer;
    }

    public String constructCustomersUrl(String hash) {
        return host + "user/list/?hash=" + hash;
    }

    public String constructCustomerUrl(String hash, String customerId, String s) { return host + s + customerId + "&hash=" + hash; }

    //for testing purposes
    public void setHost(String host) {
        this.host = host;
    }

}
