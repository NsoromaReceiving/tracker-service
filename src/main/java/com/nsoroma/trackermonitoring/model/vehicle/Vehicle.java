
package com.nsoroma.trackermonitoring.model.vehicle;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "avatar_file_name",
    "tracker_id",
    "label",
    "max_speed",
    "model",
    "type",
    "subtype",
    "garage_id",
    "reg_number",
    "vin",
    "chassis_number",
    "payload_weight",
    "payload_height",
    "payload_length",
    "payload_width",
    "passengers",
    "fuel_type",
    "fuel_grade",
    "norm_avg_fuel_consumption",
    "fuel_tank_volume",
    "wheel_arrangement",
    "tyre_size",
    "tyres_number",
    "liability_insurance_policy_number",
    "liability_insurance_valid_till",
    "free_insurance_policy_number",
    "free_insurance_valid_till"
})
public class Vehicle {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("avatar_file_name")
    private String avatarFileName;
    @JsonProperty("tracker_id")
    private Integer trackerId;
    @JsonProperty("label")
    private String label;
    @JsonProperty("max_speed")
    private Integer maxSpeed;
    @JsonProperty("model")
    private String model;
    @JsonProperty("type")
    private String type;
    @JsonProperty("subtype")
    private String subtype;
    @JsonProperty("garage_id")
    private Integer garageId;
    @JsonProperty("reg_number")
    private String regNumber;
    @JsonProperty("vin")
    private String vin;
    @JsonProperty("chassis_number")
    private String chassisNumber;
    @JsonProperty("payload_weight")
    private Integer payloadWeight;
    @JsonProperty("payload_height")
    private Integer payloadHeight;
    @JsonProperty("payload_length")
    private Integer payloadLength;
    @JsonProperty("payload_width")
    private Integer payloadWidth;
    @JsonProperty("passengers")
    private Integer passengers;
    @JsonProperty("fuel_type")
    private String fuelType;
    @JsonProperty("fuel_grade")
    private String fuelGrade;
    @JsonProperty("norm_avg_fuel_consumption")
    private Double normAvgFuelConsumption;
    @JsonProperty("fuel_tank_volume")
    private Integer fuelTankVolume;
    @JsonProperty("wheel_arrangement")
    private String wheelArrangement;
    @JsonProperty("tyre_size")
    private String tyreSize;
    @JsonProperty("tyres_number")
    private Integer tyresNumber;
    @JsonProperty("liability_insurance_policy_number")
    private String liabilityInsurancePolicyNumber;
    @JsonProperty("liability_insurance_valid_till")
    private String liabilityInsuranceValidTill;
    @JsonProperty("free_insurance_policy_number")
    private String freeInsurancePolicyNumber;
    @JsonProperty("free_insurance_valid_till")
    private Object freeInsuranceValidTill;

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("avatar_file_name")
    public String getAvatarFileName() {
        return avatarFileName;
    }

    @JsonProperty("avatar_file_name")
    public void setAvatarFileName(String avatarFileName) {
        this.avatarFileName = avatarFileName;
    }

    @JsonProperty("tracker_id")
    public Integer getTrackerId() {
        return trackerId;
    }

    @JsonProperty("tracker_id")
    public void setTrackerId(Integer trackerId) {
        this.trackerId = trackerId;
    }

    @JsonProperty("label")
    public String getLabel() {
        return label;
    }

    @JsonProperty("label")
    public void setLabel(String label) {
        this.label = label;
    }

    @JsonProperty("max_speed")
    public Integer getMaxSpeed() {
        return maxSpeed;
    }

    @JsonProperty("max_speed")
    public void setMaxSpeed(Integer maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    @JsonProperty("model")
    public String getModel() {
        return model;
    }

    @JsonProperty("model")
    public void setModel(String model) {
        this.model = model;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("subtype")
    public String getSubtype() {
        return subtype;
    }

    @JsonProperty("subtype")
    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    @JsonProperty("garage_id")
    public Integer getGarageId() {
        return garageId;
    }

    @JsonProperty("garage_id")
    public void setGarageId(Integer garageId) {
        this.garageId = garageId;
    }

    @JsonProperty("reg_number")
    public String getRegNumber() {
        return regNumber;
    }

    @JsonProperty("reg_number")
    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    @JsonProperty("vin")
    public String getVin() {
        return vin;
    }

    @JsonProperty("vin")
    public void setVin(String vin) {
        this.vin = vin;
    }

    @JsonProperty("chassis_number")
    public String getChassisNumber() {
        return chassisNumber;
    }

    @JsonProperty("chassis_number")
    public void setChassisNumber(String chassisNumber) {
        this.chassisNumber = chassisNumber;
    }

    @JsonProperty("payload_weight")
    public Integer getPayloadWeight() {
        return payloadWeight;
    }

    @JsonProperty("payload_weight")
    public void setPayloadWeight(Integer payloadWeight) {
        this.payloadWeight = payloadWeight;
    }

    @JsonProperty("payload_height")
    public Integer getPayloadHeight() {
        return payloadHeight;
    }

    @JsonProperty("payload_height")
    public void setPayloadHeight(Integer payloadHeight) {
        this.payloadHeight = payloadHeight;
    }

    @JsonProperty("payload_length")
    public Integer getPayloadLength() {
        return payloadLength;
    }

    @JsonProperty("payload_length")
    public void setPayloadLength(Integer payloadLength) {
        this.payloadLength = payloadLength;
    }

    @JsonProperty("payload_width")
    public Integer getPayloadWidth() {
        return payloadWidth;
    }

    @JsonProperty("payload_width")
    public void setPayloadWidth(Integer payloadWidth) {
        this.payloadWidth = payloadWidth;
    }

    @JsonProperty("passengers")
    public Integer getPassengers() {
        return passengers;
    }

    @JsonProperty("passengers")
    public void setPassengers(Integer passengers) {
        this.passengers = passengers;
    }

    @JsonProperty("fuel_type")
    public String getFuelType() {
        return fuelType;
    }

    @JsonProperty("fuel_type")
    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    @JsonProperty("fuel_grade")
    public String getFuelGrade() {
        return fuelGrade;
    }

    @JsonProperty("fuel_grade")
    public void setFuelGrade(String fuelGrade) {
        this.fuelGrade = fuelGrade;
    }

    @JsonProperty("norm_avg_fuel_consumption")
    public Double getNormAvgFuelConsumption() {
        return normAvgFuelConsumption;
    }

    @JsonProperty("norm_avg_fuel_consumption")
    public void setNormAvgFuelConsumption(Double normAvgFuelConsumption) {
        this.normAvgFuelConsumption = normAvgFuelConsumption;
    }

    @JsonProperty("fuel_tank_volume")
    public Integer getFuelTankVolume() {
        return fuelTankVolume;
    }

    @JsonProperty("fuel_tank_volume")
    public void setFuelTankVolume(Integer fuelTankVolume) {
        this.fuelTankVolume = fuelTankVolume;
    }

    @JsonProperty("wheel_arrangement")
    public String getWheelArrangement() {
        return wheelArrangement;
    }

    @JsonProperty("wheel_arrangement")
    public void setWheelArrangement(String wheelArrangement) {
        this.wheelArrangement = wheelArrangement;
    }

    @JsonProperty("tyre_size")
    public String getTyreSize() {
        return tyreSize;
    }

    @JsonProperty("tyre_size")
    public void setTyreSize(String tyreSize) {
        this.tyreSize = tyreSize;
    }

    @JsonProperty("tyres_number")
    public Integer getTyresNumber() {
        return tyresNumber;
    }

    @JsonProperty("tyres_number")
    public void setTyresNumber(Integer tyresNumber) {
        this.tyresNumber = tyresNumber;
    }

    @JsonProperty("liability_insurance_policy_number")
    public String getLiabilityInsurancePolicyNumber() {
        return liabilityInsurancePolicyNumber;
    }

    @JsonProperty("liability_insurance_policy_number")
    public void setLiabilityInsurancePolicyNumber(String liabilityInsurancePolicyNumber) {
        this.liabilityInsurancePolicyNumber = liabilityInsurancePolicyNumber;
    }

    @JsonProperty("liability_insurance_valid_till")
    public String getLiabilityInsuranceValidTill() {
        return liabilityInsuranceValidTill;
    }

    @JsonProperty("liability_insurance_valid_till")
    public void setLiabilityInsuranceValidTill(String liabilityInsuranceValidTill) {
        this.liabilityInsuranceValidTill = liabilityInsuranceValidTill;
    }

    @JsonProperty("free_insurance_policy_number")
    public String getFreeInsurancePolicyNumber() {
        return freeInsurancePolicyNumber;
    }

    @JsonProperty("free_insurance_policy_number")
    public void setFreeInsurancePolicyNumber(String freeInsurancePolicyNumber) {
        this.freeInsurancePolicyNumber = freeInsurancePolicyNumber;
    }

    @JsonProperty("free_insurance_valid_till")
    public Object getFreeInsuranceValidTill() {
        return freeInsuranceValidTill;
    }

    @JsonProperty("free_insurance_valid_till")
    public void setFreeInsuranceValidTill(Object freeInsuranceValidTill) {
        this.freeInsuranceValidTill = freeInsuranceValidTill;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("avatarFileName", avatarFileName).append("trackerId", trackerId).append("label", label).append("maxSpeed", maxSpeed).append("model", model).append("type", type).append("subtype", subtype).append("garageId", garageId).append("regNumber", regNumber).append("vin", vin).append("chassisNumber", chassisNumber).append("payloadWeight", payloadWeight).append("payloadHeight", payloadHeight).append("payloadLength", payloadLength).append("payloadWidth", payloadWidth).append("passengers", passengers).append("fuelType", fuelType).append("fuelGrade", fuelGrade).append("normAvgFuelConsumption", normAvgFuelConsumption).append("fuelTankVolume", fuelTankVolume).append("wheelArrangement", wheelArrangement).append("tyreSize", tyreSize).append("tyresNumber", tyresNumber).append("liabilityInsurancePolicyNumber", liabilityInsurancePolicyNumber).append("liabilityInsuranceValidTill", liabilityInsuranceValidTill).append("freeInsurancePolicyNumber", freeInsurancePolicyNumber).append("freeInsuranceValidTill", freeInsuranceValidTill).toString();
    }

}
