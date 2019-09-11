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

    @Value("${nsoromagps.server2.panelAPI.host}")
    String host;

    @Override
    public List<Customer> getCustomers(String hash) throws IOException {
        String customerUrl = host + "user/list/?hash=" + hash;
        List<Customer> customerList = new ArrayList<Customer>();
        try  {
            HttpResponse<JsonNode> customerResponse = Unirest.get(customerUrl).header("accept", "application/json").asJson();
            if(customerResponse.getStatus() == 200) {
                ObjectMapper objectMapper = new ObjectMapper();
                String customerObjectString = customerResponse.getBody().getObject().getJSONArray("list").toString();
                customerList = Arrays.asList(objectMapper.readValue(customerObjectString, Customer[].class));
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return customerList;
    }

    @Override
    public String getCustomerHash(String hash, String customerId) throws IOException {
        String customerSessionURL = host + "user/session/create/?user_id=" + customerId + "&hash=" + hash;
        String customerHash = new String();
        try {
            HttpResponse<JsonNode> customerSessionResponse = Unirest.get(customerSessionURL).header("accept", "application/json").asJson();
            if(customerSessionResponse.getStatus() == 200) {
                customerHash = customerSessionResponse.getBody().getObject().getString("hash");
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return customerHash;
    }
}
