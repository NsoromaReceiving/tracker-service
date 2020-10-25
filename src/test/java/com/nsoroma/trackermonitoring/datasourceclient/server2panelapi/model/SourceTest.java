package com.nsoroma.trackermonitoring.datasourceclient.server2panelapi.model;

import org.junit.Test;
import org.meanbean.test.BeanTester;
import org.meanbean.test.Configuration;
import org.meanbean.test.ConfigurationBuilder;

public class SourceTest {
    @Test
    public void testSlimCustomer() {
        Configuration configuration = new ConfigurationBuilder().iterations(1).build();
        new BeanTester().testBean(Source.class, configuration);

        Source source = new Source();
        source.setTariffEndDate("endDate");
        source.setPhone("phone");
        source.setBlocked(false);

        Source source1 = new Source();
        source1.setTariffEndDate(source.getTariffEndDate());
        source1.setPhone(source.getPhone());
        source1.setBlocked(source.getBlocked());

        assertEquals(source1.toString(), source.toString());
    }
}