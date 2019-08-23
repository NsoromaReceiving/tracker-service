package com.nsoroma.trackermonitoring.restcontrollers;

import java.util.Collections;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.nsoroma.trackermonitoring.services.Trackers;
import com.nsoroma.trackermonitoring.model.trackerstate.TrackerState;

@RestController
public class VehiclesController {

    @Autowired
    Trackers trackers;


    @RequestMapping(value = "/vehicles")
    public ResponseEntity<Set<TrackerState>> getVehicles() {
        Set<TrackerState> state = Collections.EMPTY_SET;

        state = trackers.getTrackers();

        return new ResponseEntity<>(state, HttpStatus.OK);
    }
}