package com.nsoroma.trackermonitoring.datasourceclient.server2panelapi.model;

import org.junit.Test;
import org.meanbean.test.BeanTester;
import org.meanbean.test.Configuration;
import org.meanbean.test.ConfigurationBuilder;

public class TrackerTest {
    @Test
    public void testSlimCustomer() {
        Configuration configuration = new ConfigurationBuilder().iterations(1).build();
        new BeanTester().testBean(Tracker.class, configuration);

        Tracker tracker = new Tracker();
        tracker.setDeleted(false);
        tracker.setId(123131);

        Tracker tracker1 = new Tracker();
        tracker1.setId(tracker.getId());
        tracker1.setDeleted(tracker.getDeleted());

        assertEquals(tracker1.toString(), tracker.toString());
    }
}