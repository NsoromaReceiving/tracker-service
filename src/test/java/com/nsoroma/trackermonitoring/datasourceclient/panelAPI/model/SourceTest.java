package com.nsoroma.trackermonitoring.datasourceclient.panelAPI.model;

import org.junit.Test;
import org.meanbean.test.BeanTester;
import org.meanbean.test.Configuration;
import org.meanbean.test.ConfigurationBuilder;

import static org.junit.Assert.*;

public class SourceTest {
    @Test
    public void testSlimCustomer() {
        Configuration configuration = new ConfigurationBuilder().ignoreProperty("clone").iterations(1).build();
        new BeanTester().testBean(Source.class, configuration);
    }
}