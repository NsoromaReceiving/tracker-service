package com.nsoroma.trackermonitoring.datasourceclient.server1api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter @JsonIgnoreProperties(ignoreUnknown = true)
public class Unit implements Serializable {

    @JsonProperty("Name")
    private String name;

    public void setImei(String imei) {
        if(imei != null) {
            this.imei = imei;
        } else {
            this.imei = "";
        }
    }

    @JsonProperty("IMEI")
    private String imei;

    @JsonProperty("Status")
    private String status;

    @JsonProperty("GroupName")
    private String groupName;

}
