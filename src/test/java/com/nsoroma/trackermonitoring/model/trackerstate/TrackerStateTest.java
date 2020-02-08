package com.nsoroma.trackermonitoring.model.trackerstate;

import org.junit.Test;
import org.meanbean.test.BeanTester;
import org.meanbean.test.Configuration;
import org.meanbean.test.ConfigurationBuilder;


public class TrackerStateTest {
    @Test
    public void testSlimCustomer() {
        Configuration configuration = new ConfigurationBuilder().iterations(1).build();
        new BeanTester().testBean(TrackerState.class, configuration);
    }
}