package com.nsoroma.trackermonitoring.datasourceclient.panelAPI.client;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.nsoroma.trackermonitoring.datasourceclient.panelAPI.model.Customer;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomerServiceImpl implements CustomerService{

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
}
