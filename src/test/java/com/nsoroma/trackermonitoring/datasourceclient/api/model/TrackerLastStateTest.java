package com.nsoroma.trackermonitoring.datasourceclient.api.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Test;
import org.meanbean.test.BeanTester;
import org.meanbean.test.Configuration;
import org.meanbean.test.ConfigurationBuilder;

import java.util.Collections;

import static org.junit.Assert.*;

public class TrackerLastStateTest {
    @Test
    public void testObjectEquality() {
        TrackerLastState trackerLastState = new TrackerLastState();
        trackerLastState.setGps(new Gps());
        trackerLastState.setBatteryLevel(100);
        trackerLastState.setGsm(new Gsm());
        trackerLastState.setTrackerId("213114");
        trackerLastState.setActualTrackUpdate("actualUpdateDate");
        trackerLastState.setBatteryUpdate("batteryUpdate");
        trackerLastState.setConnectionStatus("connectionStatus");
        trackerLastState.setInputsUpdate("inputUpdate");
        trackerLastState.setInputs(Collections.emptyList());
        trackerLastState.setMovementStatus("stopped");
        trackerLastState.setOutputs(Collections.emptyList());
        trackerLastState.setOutputsUpdate("outputUpdate");
        trackerLastState.setLastUpdate("lastUpdate");
        trackerLastState.setSourceId(78909);

        TrackerLastState trackerLastState1 = new TrackerLastState();
        trackerLastState1.setGps(trackerLastState.getGps());
        trackerLastState1.setBatteryLevel(trackerLastState.getBatteryLevel());
        trackerLastState1.setGsm(trackerLastState.getGsm());
        trackerLastState1.setTrackerId(trackerLastState.getTrackerId());
        trackerLastState1.setActualTrackUpdate(trackerLastState.getActualTrackUpdate());
        trackerLastState1.setBatteryUpdate(trackerLastState.getBatteryUpdate());
        trackerLastState1.setConnectionStatus(trackerLastState.getConnectionStatus());
        trackerLastState1.setInputsUpdate(trackerLastState.getInputsUpdate());
        trackerLastState1.setInputs(trackerLastState.getInputs());
        trackerLastState1.setMovementStatus(trackerLastState.getMovementStatus());
        trackerLastState1.setOutputs(trackerLastState.getOutputs());
        trackerLastState1.setOutputsUpdate(trackerLastState.getOutputsUpdate());
        trackerLastState1.setLastUpdate(trackerLastState.getLastUpdate());
        trackerLastState1.setSourceId(trackerLastState.getSourceId());

        assertTrue(EqualsBuilder.reflectionEquals(trackerLastState1, trackerLastState));
        assertEquals(trackerLastState1.toString(), trackerLastState.toString());
    }
}