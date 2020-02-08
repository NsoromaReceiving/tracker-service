package com.nsoroma.trackermonitoring.model.schedule;

import org.junit.Test;
import org.meanbean.test.BeanTester;
import org.meanbean.test.Configuration;
import org.meanbean.test.ConfigurationBuilder;

import static org.junit.Assert.*;

public class ScheduleTest {

    @Test
    public void testSlimCustomer() {
        Configuration configuration = new ConfigurationBuilder().ignoreProperty("zoneId").ignoreProperty("alertTime").iterations(1).build();
        new BeanTester().testBean(Schedule.class, configuration);
    }
}