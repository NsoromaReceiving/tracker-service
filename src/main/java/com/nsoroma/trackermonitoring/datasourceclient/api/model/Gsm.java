
package com.nsoroma.trackermonitoring.datasourceclient.api.model; ;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "updated",
    "signal_level",
    "network_name",
    "roaming"
})
public class Gsm {

    @JsonProperty("updated")
    private String updated;
    @JsonProperty("signal_level")
    private Integer signalLevel;
    @JsonProperty("network_name")
    private String networkName;
    @JsonProperty("roaming")
    private Boolean roaming;

    @JsonProperty("updated")
    public String getUpdated() {
        return updated;
    }

    @JsonProperty("updated")
    public void setUpdated(String updated) {
        this.updated = updated;
    }

    @JsonProperty("signal_level")
    public Integer getSignalLevel() {
        return signalLevel;
    }

    @JsonProperty("signal_level")
    public void setSignalLevel(Integer signalLevel) {
        this.signalLevel = signalLevel;
    }

    @JsonProperty("network_name")
    public String getNetworkName() {
        return networkName;
    }

    @JsonProperty("network_name")
    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }

    @JsonProperty("roaming")
    public Boolean getRoaming() {
        return roaming;
    }

    @JsonProperty("roaming")
    public void setRoaming(Boolean roaming) {
        this.roaming = roaming;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("updated", updated).append("signalLevel", signalLevel).append("networkName", networkName).append("roaming", roaming).toString();
    }

}
