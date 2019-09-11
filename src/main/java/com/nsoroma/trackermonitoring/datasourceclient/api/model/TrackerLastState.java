
package com.nsoroma.trackermonitoring.datasourceclient.api.model; ;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "source_id",
    "gps",
    "connection_status",
    "movement_status",
    "gsm",
    "last_update",
    "battery_level",
    "battery_update",
    "inputs",
    "inputs_update",
    "outputs",
    "outputs_update",
    "actual_track_update"
})
public class TrackerLastState {

    private String trackerId;
    @JsonProperty("source_id")
    private Integer sourceId;
    @JsonProperty("gps")
    private Gps gps;
    @JsonProperty("connection_status")
    private String connectionStatus;
    @JsonProperty("movement_status")
    private String movementStatus;
    @JsonProperty("gsm")
    private Gsm gsm;
    @JsonProperty("last_update")
    private String lastUpdate;
    @JsonProperty("battery_level")
    private Integer batteryLevel;
    @JsonProperty("battery_update")
    private String batteryUpdate;
    @JsonProperty("inputs")
    private List<Boolean> inputs = null;
    @JsonProperty("inputs_update")
    private String inputsUpdate;
    @JsonProperty("outputs")
    private List<Boolean> outputs = null;
    @JsonProperty("outputs_update")
    private String outputsUpdate;
    @JsonProperty("actual_track_update")
    private String actualTrackUpdate;

    @JsonProperty("source_id")
    public Integer getSourceId() {
        return sourceId;
    }

    @JsonProperty("source_id")
    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    @JsonProperty("gps")
    public Gps getGps() {
        return gps;
    }

    @JsonProperty("gps")
    public void setGps(Gps gps) {
        this.gps = gps;
    }

    @JsonProperty("connection_status")
    public String getConnectionStatus() {
        return connectionStatus;
    }

    @JsonProperty("connection_status")
    public void setConnectionStatus(String connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    @JsonProperty("movement_status")
    public String getMovementStatus() {
        return movementStatus;
    }

    @JsonProperty("movement_status")
    public void setMovementStatus(String movementStatus) {
        this.movementStatus = movementStatus;
    }

    @JsonProperty("gsm")
    public Gsm getGsm() {
        return gsm;
    }

    @JsonProperty("gsm")
    public void setGsm(Gsm gsm) {
        this.gsm = gsm;
    }

    @JsonProperty("last_update")
    public String getLastUpdate() {
        return lastUpdate;
    }

    @JsonProperty("last_update")
    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @JsonProperty("battery_level")
    public Integer getBatteryLevel() {
        return batteryLevel;
    }

    @JsonProperty("battery_level")
    public void setBatteryLevel(Integer batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    @JsonProperty("battery_update")
    public String getBatteryUpdate() {
        return batteryUpdate;
    }

    @JsonProperty("battery_update")
    public void setBatteryUpdate(String batteryUpdate) {
        this.batteryUpdate = batteryUpdate;
    }

    @JsonProperty("inputs")
    public List<Boolean> getInputs() {
        return inputs;
    }

    @JsonProperty("inputs")
    public void setInputs(List<Boolean> inputs) {
        this.inputs = inputs;
    }

    @JsonProperty("inputs_update")
    public String getInputsUpdate() {
        return inputsUpdate;
    }

    @JsonProperty("inputs_update")
    public void setInputsUpdate(String inputsUpdate) {
        this.inputsUpdate = inputsUpdate;
    }

    @JsonProperty("outputs")
    public List<Boolean> getOutputs() {
        return outputs;
    }

    @JsonProperty("outputs")
    public void setOutputs(List<Boolean> outputs) {
        this.outputs = outputs;
    }

    @JsonProperty("outputs_update")
    public String getOutputsUpdate() {
        return outputsUpdate;
    }

    @JsonProperty("outputs_update")
    public void setOutputsUpdate(String outputsUpdate) {
        this.outputsUpdate = outputsUpdate;
    }

    @JsonProperty("actual_track_update")
    public String getActualTrackUpdate() {
        return actualTrackUpdate;
    }

    @JsonProperty("actual_track_update")
    public void setActualTrackUpdate(String actualTrackUpdate) {
        this.actualTrackUpdate = actualTrackUpdate;
    }

    public String getTrackerId() { return trackerId; }

    public void setTrackerId(String trackerId) { this.trackerId = trackerId; }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("sourceId", sourceId).append("gps", gps).append("connectionStatus", connectionStatus).append("movementStatus", movementStatus).append("gsm", gsm).append("lastUpdate", lastUpdate).append("batteryLevel", batteryLevel).append("batteryUpdate", batteryUpdate).append("inputs", inputs).append("inputsUpdate", inputsUpdate).append("outputs", outputs).append("outputsUpdate", outputsUpdate).append("actualTrackUpdate", actualTrackUpdate).append("trackerId",trackerId).toString();
    }

}
