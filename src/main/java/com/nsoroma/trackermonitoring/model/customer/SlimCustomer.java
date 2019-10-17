package com.nsoroma.trackermonitoring.model.customer;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class SlimCustomer {

    private String customerId;
    private String customerName;
    private String login;


    public String getCustomerId() {
        return customerId;
    }
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }


}

