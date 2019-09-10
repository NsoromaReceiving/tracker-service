
package com.nsoroma.trackermonitoring.datasourceclient.api.model; ;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "updated",
    "signal_level",
    "location",
    "heading",
    "speed",
    "alt"
})
public class Gps {

    @JsonProperty("updated")
    private String updated;
    @JsonProperty("signal_level")
    private Integer signalLevel;
    @JsonProperty("location")
    private Location location;
    @JsonProperty("heading")
    private Integer heading;
    @JsonProperty("speed")
    private Integer speed;
    @JsonProperty("alt")
    private Integer alt;

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

    @JsonProperty("location")
    public Location getLocation() {
        return location;
    }

    @JsonProperty("location")
    public void setLocation(Location location) {
        this.location = location;
    }

    @JsonProperty("heading")
    public Integer getHeading() {
        return heading;
    }

    @JsonProperty("heading")
    public void setHeading(Integer heading) {
        this.heading = heading;
    }

    @JsonProperty("speed")
    public Integer getSpeed() {
        return speed;
    }

    @JsonProperty("speed")
    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    @JsonProperty("alt")
    public Integer getAlt() {
        return alt;
    }

    @JsonProperty("alt")
    public void setAlt(Integer alt) {
        this.alt = alt;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("updated", updated).append("signalLevel", signalLevel).append("location", location).append("heading", heading).append("speed", speed).append("alt", alt).toString();
    }

}
