package com.nsoroma.trackermonitoring.datasourceclient.api.model;

import org.junit.Test;
import org.meanbean.test.BeanTester;
import org.meanbean.test.Configuration;
import org.meanbean.test.ConfigurationBuilder;

import static org.junit.Assert.*;

public class GpsTest {
    @Test
    public void testSlimCustomer() {
        Configuration configuration = new ConfigurationBuilder().iterations(1).build();
        new BeanTester().testBean(Gps.class, configuration);
    }
}