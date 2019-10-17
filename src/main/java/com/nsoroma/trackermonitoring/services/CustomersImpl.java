package com.nsoroma.trackermonitoring.services;

import com.nsoroma.trackermonitoring.datasourceclient.panelAPI.client.PanelApiAuthentication;
import com.nsoroma.trackermonitoring.datasourceclient.panelAPI.client.PanelApiCustomerService;
import com.nsoroma.trackermonitoring.datasourceclient.panelAPI.model.Customer;
import com.nsoroma.trackermonitoring.model.customer.SlimCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class CustomersImpl implements Customers {

    @Autowired
    private PanelApiAuthentication apiAuthentication;

    @Autowired
    private PanelApiCustomerService apiCustomerService;

    @Override
    public List<SlimCustomer> getCustomers() throws IOException {
        String hash = apiAuthentication.getDealerHash();
        List<Customer> customers = apiCustomerService.getCustomers(hash);

        List<SlimCustomer> slimCustomers = new ArrayList<>();
        for(Customer customer: customers) {
            SlimCustomer slimCustomer = new SlimCustomer();
            slimCustomer.setCustomerId(customer.getId().toString());
            String customerName = customer.getFirstName() + " " + customer.getMiddleName() + " " + customer.getLastName();
            slimCustomer.setCustomerName(customerName);
            slimCustomer.setLogin(customer.getLogin());
            slimCustomers.add(slimCustomer);
        }
        System.out.println(slimCustomers);
        Comparator<SlimCustomer> compareByName = (SlimCustomer name1, SlimCustomer name2) -> name1.getCustomerName().compareToIgnoreCase(name2.getCustomerName());
        slimCustomers.sort(compareByName);
        return slimCustomers;
    }
}
