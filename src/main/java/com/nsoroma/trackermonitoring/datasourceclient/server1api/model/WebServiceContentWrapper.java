package com.nsoroma.trackermonitoring.datasourceclient.server1api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WebServiceContentWrapper {

    private WebServiceDataWrapper data;

    @JsonProperty("data")
    public WebServiceDataWrapper getData() {
        return data;
    }
    public void setData(WebServiceDataWrapper data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "WebserviceContent [data=" + data + "]";
    }


}
