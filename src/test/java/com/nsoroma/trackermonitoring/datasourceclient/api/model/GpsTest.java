package com.nsoroma.trackermonitoring.datasourceclient.api.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Test;
import org.meanbean.test.BeanTester;
import org.meanbean.test.Configuration;
import org.meanbean.test.ConfigurationBuilder;

import static org.junit.Assert.*;

public class GpsTest {
    @Test
    public void testObjectEquality() {
        Location location = new Location();
        location.setLng(-0.352342);
        location.setLat(0.234242);

        Location location1 = new Location();
        location1.setLat(location.getLat());
        location1.setLng(location.getLng());

        Gps gps1 = new Gps();
        gps1.setLocation(location);
        gps1.setSignalLevel(89);
        gps1.setUpdated("lastUpdate");
        gps1.setAlt(23);
        gps1.setHeading(16);
        gps1.setSpeed(50);

        Gps gps2 = new Gps();
        gps2.setLocation(gps1.getLocation());
        gps2.setSignalLevel(gps1.getSignalLevel());
        gps2.setUpdated(gps1.getUpdated());
        gps2.setAlt(gps1.getAlt());
        gps2.setHeading(gps1.getHeading());
        gps2.setSpeed(gps1.getSpeed());

        assertTrue(EqualsBuilder.reflectionEquals(gps2, gps1));
        assertEquals(gps1.toString(), gps2.toString());
        assertEquals(location1.toString(), location.toString());
    }
}