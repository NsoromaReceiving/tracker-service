package com.nsoroma.trackermonitoring.model.customer;

import org.junit.Test;
import org.meanbean.test.BeanTester;
import org.meanbean.test.Configuration;
import org.meanbean.test.ConfigurationBuilder;

public class SlimCustomerTest {


    @Test
    public void testSlimCustomer() {
        Configuration configuration = new ConfigurationBuilder().iterations(1).build();
        new BeanTester().testBean(SlimCustomer.class, configuration);
    }
}