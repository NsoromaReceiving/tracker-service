package com.nsoroma.trackermonitoring.restcontrollers;

import java.util.List;
import org.json.JSONObject;
import org.json.JSONArray;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.nsoroma.trackermonitoring.datasourceclient.Vehicles;;

@RestController
public class VehiclesController {

    @Autowired
    Vehicles vehiclesClient;

    private JSONArray vehicles;


    @RequestMapping(value = "/vehicles")
    public ResponseEntity<String> getVehicles() {
        vehicles = vehiclesClient.getVehicles();
        return new ResponseEntity<String>(vehicles.toString(), HttpStatus.OK);
    }
}