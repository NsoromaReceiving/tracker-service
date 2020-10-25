package com.nsoroma.trackermonitoring.datasourceclient.server1api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import java.util.Collections;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WebServiceDataWrapper {
    private List<Unit> units = Collections.EMPTY_LIST;
    private List<LatestLocation> latestLocations = Collections.EMPTY_LIST;

    @JacksonXmlElementWrapper(useWrapping = true)
    @JsonProperty("ArrayOfUnit")
    public List<Unit> getUnits() {
        return units;
    }

    public void setVehicles(List<Unit> units) {
        this.units = units;
    }



    @Override
    public String toString() {
        return "WebserviceData [units=" + units + "]";
    }

    @JacksonXmlElementWrapper(useWrapping = true)
    @JsonProperty("ArrayOfLocation")
    public List<LatestLocation> getLatestLocations() {
        return latestLocations;
    }

    public void setLatestLocations(List<LatestLocation> latestLocations) {
        this.latestLocations = latestLocations;
    }
}
