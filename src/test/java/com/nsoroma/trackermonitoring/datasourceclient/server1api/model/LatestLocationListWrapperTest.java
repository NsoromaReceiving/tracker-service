package com.nsoroma.trackermonitoring.datasourceclient.server1api.model;

import org.junit.Test;
import org.meanbean.test.BeanTester;
import org.meanbean.test.Configuration;
import org.meanbean.test.ConfigurationBuilder;

import static org.junit.Assert.*;

public class LatestLocationListWrapperTest {

    @Test
    public void testLatestLocationWrapper() {
        Configuration configuration = new ConfigurationBuilder().iterations(1).build();
        new BeanTester().testBean(LatestLocationListWrapper.class, configuration);
    }
}