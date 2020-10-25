package com.nsoroma.trackermonitoring.datasourceclient.server1api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter @JsonIgnoreProperties(ignoreUnknown = true)
public class LatestLocation implements Serializable {

    @JsonProperty("Name")
    private String name;

    @JsonProperty("IMEI")
    private String imei;

    @JsonProperty("Odometer")
    private String odometer;

    @JsonProperty("Latitude")
    private String latitude;

    @JsonProperty("Longitude")
    private String longitude;

    @JsonProperty("DateTime")
    private String dateTime;

    @JsonProperty("Speed")
    private String speed;

    @JsonProperty("EngineStatus")
    private String engineStatus;

}
