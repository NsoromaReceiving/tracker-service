package com.nsoroma.trackermonitoring.datasourceclient.panelAPI.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Test;
import org.meanbean.test.*;

import static org.junit.Assert.*;

public class CustomerTest {
    @Test
    public void testSlimCustomer() {
        Configuration configuration = new ConfigurationBuilder().iterations(1).build();
        new BeanTester().testBean(Customer.class, configuration);

        Customer customer = new Customer();
        customer.setLogin("someLogin");
        customer.setFirstName("someName");
        customer.setActivated(true);
        customer.setVerified(true);
        customer.setPhoneVerified(true);

        Customer customer1 = new Customer();
        customer1.setLogin(customer.getLogin());
        customer1.setFirstName(customer.getFirstName());
        customer1.setActivated(customer.getActivated());
        customer1.setVerified(customer.getVerified());
        customer1.setPhoneVerified(customer.getPhoneVerified());


        assertTrue(EqualsBuilder.reflectionEquals(customer1, customer));
        assertEquals(customer1.toString(), customer.toString());


    }



}