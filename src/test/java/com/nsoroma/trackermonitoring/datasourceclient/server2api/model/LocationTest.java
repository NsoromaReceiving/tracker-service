package com.nsoroma.trackermonitoring.datasourceclient.server2api.model;

import org.junit.Test;
import org.meanbean.test.BeanTester;
import org.meanbean.test.Configuration;
import org.meanbean.test.ConfigurationBuilder;

public class LocationTest {
    @Test
    public void testSlimCustomer() {
        Configuration configuration = new ConfigurationBuilder().iterations(1).build();
        new BeanTester().testBean(Location.class, configuration);
    }
}