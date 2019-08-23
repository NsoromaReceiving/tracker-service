package com.nsoroma.trackermonitoring.model.tracker;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "trackerId",
    "vehicleLabel",
    "regNumber",
    "lastUpdateDate",
    "gpsCoords",
    "signalLevel",
    "connectionStatus",
    "movementStatus",
    "gsmSignalLevel",
    "networkName"
})
public class Tracker {

    @JsonProperty("trackerId")
    private String trackerId;
    @JsonProperty("vehicleLabel")
    private String vehicleLabel;
    @JsonProperty("regNumber")
    private String regNumber;
    @JsonProperty("lastUpdateDate")
    private String lastUpdateDate;
    @JsonProperty("gpsCoords")
    private GpsCoords gpsCoords;
    @JsonProperty("signalLevel")
    private Integer signalLevel;
    @JsonProperty("connectionStatus")
    private String connectionStatus;
    @JsonProperty("movementStatus")
    private String movementStatus;
    @JsonProperty("gsmSignalLevel")
    private Integer gsmSignalLevel;
    @JsonProperty("networkName")
    private String networkName;

    @JsonProperty("trackerId")
    public String getTrackerId() {
        return trackerId;
    }

    @JsonProperty("trackerId")
    public void setTrackerId(String trackerId) {
        this.trackerId = trackerId;
    }

    @JsonProperty("vehicleLabel")
    public String getVehicleLabel() {
        return vehicleLabel;
    }

    @JsonProperty("vehicleLabel")
    public void setVehicleLabel(String vehicleLabel) {
        this.vehicleLabel = vehicleLabel;
    }

    @JsonProperty("regNumber")
    public String getRegNumber() {
        return regNumber;
    }

    @JsonProperty("regNumber")
    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    @JsonProperty("lastUpdateDate")
    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    @JsonProperty("lastUpdateDate")
    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    @JsonProperty("gpsCoords")
    public GpsCoords getGpsCoords() {
        return gpsCoords;
    }

    @JsonProperty("gpsCoords")
    public void setGpsCoords(GpsCoords gpsCoords) {
        this.gpsCoords = gpsCoords;
    }

    @JsonProperty("signalLevel")
    public Integer getSignalLevel() {
        return signalLevel;
    }

    @JsonProperty("signalLevel")
    public void setSignalLevel(Integer signalLevel) {
        this.signalLevel = signalLevel;
    }

    @JsonProperty("connectionStatus")
    public String getConnectionStatus() {
        return connectionStatus;
    }

    @JsonProperty("connectionStatus")
    public void setConnectionStatus(String connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    @JsonProperty("movementStatus")
    public String getMovementStatus() {
        return movementStatus;
    }

    @JsonProperty("movementStatus")
    public void setMovementStatus(String movementStatus) {
        this.movementStatus = movementStatus;
    }

    @JsonProperty("gsmSignalLevel")
    public Integer getGsmSignalLevel() {
        return gsmSignalLevel;
    }

    @JsonProperty("gsmSignalLevel")
    public void setGsmSignalLevel(Integer gsmSignalLevel) {
        this.gsmSignalLevel = gsmSignalLevel;
    }

    @JsonProperty("networkName")
    public String getNetworkName() {
        return networkName;
    }

    @JsonProperty("networkName")
    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("trackerId", trackerId).append("vehicleLabel", vehicleLabel).append("regNumber", regNumber).append("lastUpdateDate", lastUpdateDate).append("gpsCoords", gpsCoords).append("signalLevel", signalLevel).append("connectionStatus", connectionStatus).append("movementStatus", movementStatus).append("gsmSignalLevel", gsmSignalLevel).append("networkName", networkName).toString();
    }

}
