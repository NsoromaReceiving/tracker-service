package com.nsoroma.trackermonitoring.datasourceclient.server1api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import java.util.Collections;
import java.util.List;

public class WebServiceDataWrapper {
    private List<Unit> units = Collections.EMPTY_LIST;

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
}
