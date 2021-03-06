package com.nsoroma.trackermonitoring.datasourceclient.server1api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import java.util.Collections;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LatestLocationListWrapper {

    List<LatestLocation> latestLocationList = Collections.EMPTY_LIST;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JsonProperty("Location")
    public List<LatestLocation> getLatestLocations() {
        return latestLocationList;
    }

    public void setLatestLocations(List<LatestLocation> latestLocations) {
        this.latestLocationList = latestLocations;
    }

    @Override
    public String toString() {
        return "LatestLocationList [latestLocations=" + latestLocationList + "]";
    }


}
