package com.nsoroma.trackermonitoring.datasourceclient.panelAPI.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "device_id",
        "model",
        "blocked",
        "tariff_id",
        "phone",
        "status_listing_id",
        "creation_date",
        "tariff_end_date",
        "connection_status"
})
public class Source {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("device_id")
    private String deviceId;
    @JsonProperty("model")
    private String model;
    @JsonProperty("blocked")
    private Boolean blocked;
    @JsonProperty("tariff_id")
    private Integer tariffId;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("status_listing_id")
    private Object statusListingId;
    @JsonProperty("creation_date")
    private String creationDate;
    @JsonProperty("tariff_end_date")
    private String tariffEndDate;
    @JsonProperty("connection_status")
    private String connectionStatus;

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("device_id")
    public String getDeviceId() {
        return deviceId;
    }

    @JsonProperty("device_id")
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @JsonProperty("model")
    public String getModel() {
        return model;
    }

    @JsonProperty("model")
    public void setModel(String model) {
        this.model = model;
    }

    @JsonProperty("blocked")
    public Boolean getBlocked() {
        return blocked;
    }

    @JsonProperty("blocked")
    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }

    @JsonProperty("tariff_id")
    public Integer getTariffId() {
        return tariffId;
    }

    @JsonProperty("tariff_id")
    public void setTariffId(Integer tariffId) {
        this.tariffId = tariffId;
    }

    @JsonProperty("phone")
    public String getPhone() {
        return phone;
    }

    @JsonProperty("phone")
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @JsonProperty("status_listing_id")
    public Object getStatusListingId() {
        return statusListingId;
    }

    @JsonProperty("status_listing_id")
    public void setStatusListingId(Object statusListingId) {
        this.statusListingId = statusListingId;
    }

    @JsonProperty("creation_date")
    public String getCreationDate() {
        return creationDate;
    }

    @JsonProperty("creation_date")
    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    @JsonProperty("tariff_end_date")
    public String getTariffEndDate() {
        return tariffEndDate;
    }

    @JsonProperty("tariff_end_date")
    public void setTariffEndDate(String tariffEndDate) {
        this.tariffEndDate = tariffEndDate;
    }

    @JsonProperty("connection_status")
    public String getConnectionStatus() {
        return connectionStatus;
    }

    @JsonProperty("connection_status")
    public void setConnectionStatus(String connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("deviceId", deviceId).append("model", model).append("blocked", blocked).append("tariffId", tariffId).append("phone", phone).append("statusListingId", statusListingId).append("creationDate", creationDate).append("tariffEndDate", tariffEndDate).append("connectionStatus", connectionStatus).toString();
    }

}
