package com.nsoroma.trackermonitoring.datasourceclient.api.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.junit.Test;
import org.meanbean.test.BeanTester;
import org.meanbean.test.Configuration;
import org.meanbean.test.ConfigurationBuilder;

import static org.junit.Assert.*;

public class GsmTest {
    @Test
    public void testGsmObjectEquality() {
        Gsm gsm1 = new Gsm();
        gsm1.setNetworkName("MTN");
        gsm1.setUpdated("someUpdateDate");
        gsm1.setSignalLevel(100);
        gsm1.setRoaming(true);

        Gsm gsm2 = new Gsm();
        gsm2.setRoaming(gsm1.getRoaming());
        gsm2.setUpdated(gsm1.getUpdated());
        gsm2.setNetworkName(gsm1.getNetworkName());
        gsm2.setSignalLevel(gsm1.getSignalLevel());

        assertTrue(EqualsBuilder.reflectionEquals(gsm2, gsm1));
        assertEquals(gsm2.toString(), gsm1.toString());
    }
}