package com.nsoroma.trackermonitoring.services;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.nsoroma.trackermonitoring.datasourceclient.server1api.client.UnitManager;
import com.nsoroma.trackermonitoring.datasourceclient.server1api.model.Unit;
import com.nsoroma.trackermonitoring.datasourceclient.server2panelapi.client.PanelApiAuthentication;
import com.nsoroma.trackermonitoring.datasourceclient.server2panelapi.client.PanelApiCustomerService;
import com.nsoroma.trackermonitoring.datasourceclient.server2panelapi.model.Customer;
import com.nsoroma.trackermonitoring.exceptions.DataSourceClientResponseException;
import com.nsoroma.trackermonitoring.model.customer.SlimCustomer;
import com.nsoroma.trackermonitoring.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class CustomersImpl implements Customers {

    private Logger log = LoggerFactory.getLogger(CustomersImpl.class);

    @Autowired
    private PanelApiAuthentication apiAuthentication;

    @Autowired
    private PanelApiCustomerService apiCustomerService;

    @Autowired
    private UnitManager unitManager;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public void saveCustomers() throws IOException, UnirestException, DataSourceClientResponseException {
        String hash = apiAuthentication.getDealerHash();
        List<Customer> server1Customers = apiCustomerService.getCustomers(hash);
        ArrayList<String> uids = unitManager.getUnitsStringChunks();
        List<Unit> unitList = unitManager.getUnits(uids);
        List<SlimCustomer> slimCustomers = new ArrayList<>();
        for(Customer customer: server1Customers) {
            SlimCustomer slimCustomer = new SlimCustomer();
            slimCustomer.setCustomerId(customer.getId().toString());
            String customerName = customer.getFirstName() + " " + customer.getMiddleName() + " " + customer.getLastName();
            slimCustomer.setCustomerName(customerName);
            slimCustomer.setLogin(customer.getLogin());
            slimCustomers.add(slimCustomer);
        }

        for (Unit unit: unitList) {
            SlimCustomer slimCustomer = new SlimCustomer();
            slimCustomer.setCustomerId(unit.getCompany());
            slimCustomer.setCustomerName(unit.getCompany());
            if(slimCustomers.parallelStream().noneMatch(slimCustomer1 -> slimCustomer1.getCustomerName().equals(slimCustomer.getCustomerName()))){
                slimCustomers.add(slimCustomer);
            }
        }

        String slimCustomersStr = String.valueOf(slimCustomers);
        log.info(slimCustomersStr);
        Comparator<SlimCustomer> compareByName = (SlimCustomer name1, SlimCustomer name2) -> name1.getCustomerName().compareToIgnoreCase(name2.getCustomerName());
        slimCustomers.sort(compareByName);

        customerRepository.saveAll(slimCustomers);
    }

    @Override
    public List<SlimCustomer> getCustomers() throws IOException, UnirestException, DataSourceClientResponseException {
        return customerRepository.findAll();
    }

    protected void setApiAuthentication(PanelApiAuthentication apiAuthentication) {
        this.apiAuthentication = apiAuthentication;
    }

    protected void setApiCustomerService(PanelApiCustomerService apiCustomerService) {
        this.apiCustomerService = apiCustomerService;
    }

    protected void setUnitManager(UnitManager unitManager) {
        this.unitManager = unitManager;
    }

}
