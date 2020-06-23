package com.nsoroma.trackermonitoring.datasourceclient.panelAPI.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "dealer_id",
        "activated",
        "login",
        "first_name",
        "middle_name",
        "last_name",
        "legal_type",
        "post_city",
        "phone",
        "post_country",
        "post_index",
        "post_region",
        "post_street_address",
        "registered_country",
        "registered_index",
        "registered_region",
        "registered_city",
        "registered_street_address",
        "tin",
        "state_reg_num",
        "okpo_code",
        "legal_name",
        "iec",
        "balance",
        "bonus",
        "trackers_count",
        "creation_date",
        "verified",
        "phone_verified"
})
public class Customer {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("dealer_id")
    private Integer dealerId;
    @JsonProperty("activated")
    private Boolean activated;
    @JsonProperty("login")
    private String login;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("middle_name")
    private String middleName;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("legal_type")
    private String legalType;
    @JsonProperty("post_city")
    private String postCity;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("post_country")
    private String postCountry;
    @JsonProperty("post_index")
    private String postIndex;
    @JsonProperty("post_region")
    private String postRegion;
    @JsonProperty("post_street_address")
    private String postStreetAddress;
    @JsonProperty("registered_country")
    private String registeredCountry;
    @JsonProperty("registered_index")
    private String registeredIndex;
    @JsonProperty("registered_region")
    private String registeredRegion;
    @JsonProperty("registered_city")
    private String registeredCity;
    @JsonProperty("registered_street_address")
    private String registeredStreetAddress;
    @JsonProperty("tin")
    private String tin;
    @JsonProperty("state_reg_num")
    private Object stateRegNum;
    @JsonProperty("okpo_code")
    private Object okpoCode;
    @JsonProperty("legal_name")
    private String legalName;
    @JsonProperty("iec")
    private String iec;
    @JsonProperty("balance")
    private Integer balance;
    @JsonProperty("bonus")
    private Integer bonus;
    @JsonProperty("trackers_count")
    private Integer trackersCount;
    @JsonProperty("creation_date")
    private String creationDate;
    @JsonProperty("verified")
    private Boolean verified;
    @JsonProperty("phone_verified")
    private Boolean phoneVerified;

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("dealer_id")
    public Integer getDealerId() {
        return dealerId;
    }

    @JsonProperty("dealer_id")
    public void setDealerId(Integer dealerId) {
        this.dealerId = dealerId;
    }

    @JsonProperty("activated")
    public Boolean getActivated() {
        return activated;
    }

    @JsonProperty("activated")
    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    @JsonProperty("login")
    public String getLogin() {
        return login;
    }

    @JsonProperty("login")
    public void setLogin(String login) {
        this.login = login;
    }

    @JsonProperty("first_name")
    public String getFirstName() {
        return firstName;
    }

    @JsonProperty("first_name")
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @JsonProperty("middle_name")
    public String getMiddleName() {
        return middleName;
    }

    @JsonProperty("middle_name")
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    @JsonProperty("last_name")
    public String getLastName() {
        return lastName;
    }

    @JsonProperty("last_name")
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @JsonProperty("legal_type")
    public String getLegalType() {
        return legalType;
    }

    @JsonProperty("legal_type")
    public void setLegalType(String legalType) {
        this.legalType = legalType;
    }

    @JsonProperty("post_city")
    public String getPostCity() {
        return postCity;
    }

    @JsonProperty("post_city")
    public void setPostCity(String postCity) {
        this.postCity = postCity;
    }

    @JsonProperty("phone")
    public String getPhone() {
        return phone;
    }

    @JsonProperty("phone")
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @JsonProperty("post_country")
    public String getPostCountry() {
        return postCountry;
    }

    @JsonProperty("post_country")
    public void setPostCountry(String postCountry) {
        this.postCountry = postCountry;
    }

    @JsonProperty("post_index")
    public String getPostIndex() {
        return postIndex;
    }

    @JsonProperty("post_index")
    public void setPostIndex(String postIndex) {
        this.postIndex = postIndex;
    }

    @JsonProperty("post_region")
    public String getPostRegion() {
        return postRegion;
    }

    @JsonProperty("post_region")
    public void setPostRegion(String postRegion) {
        this.postRegion = postRegion;
    }

    @JsonProperty("post_street_address")
    public String getPostStreetAddress() {
        return postStreetAddress;
    }

    @JsonProperty("post_street_address")
    public void setPostStreetAddress(String postStreetAddress) {
        this.postStreetAddress = postStreetAddress;
    }

    @JsonProperty("registered_country")
    public String getRegisteredCountry() {
        return registeredCountry;
    }

    @JsonProperty("registered_country")
    public void setRegisteredCountry(String registeredCountry) {
        this.registeredCountry = registeredCountry;
    }

    @JsonProperty("registered_index")
    public String getRegisteredIndex() {
        return registeredIndex;
    }

    @JsonProperty("registered_index")
    public void setRegisteredIndex(String registeredIndex) {
        this.registeredIndex = registeredIndex;
    }

    @JsonProperty("registered_region")
    public String getRegisteredRegion() {
        return registeredRegion;
    }

    @JsonProperty("registered_region")
    public void setRegisteredRegion(String registeredRegion) {
        this.registeredRegion = registeredRegion;
    }

    @JsonProperty("registered_city")
    public String getRegisteredCity() {
        return registeredCity;
    }

    @JsonProperty("registered_city")
    public void setRegisteredCity(String registeredCity) {
        this.registeredCity = registeredCity;
    }

    @JsonProperty("registered_street_address")
    public String getRegisteredStreetAddress() {
        return registeredStreetAddress;
    }

    @JsonProperty("registered_street_address")
    public void setRegisteredStreetAddress(String registeredStreetAddress) {
        this.registeredStreetAddress = registeredStreetAddress;
    }

    @JsonProperty("tin")
    public String getTin() {
        return tin;
    }

    @JsonProperty("tin")
    public void setTin(String tin) {
        this.tin = tin;
    }

    @JsonProperty("state_reg_num")
    public Object getStateRegNum() {
        return stateRegNum;
    }

    @JsonProperty("state_reg_num")
    public void setStateRegNum(Object stateRegNum) {
        this.stateRegNum = stateRegNum;
    }

    @JsonProperty("okpo_code")
    public Object getOkpoCode() {
        return okpoCode;
    }

    @JsonProperty("okpo_code")
    public void setOkpoCode(Object okpoCode) {
        this.okpoCode = okpoCode;
    }

    @JsonProperty("legal_name")
    public String getLegalName() {
        return legalName;
    }

    @JsonProperty("legal_name")
    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    @JsonProperty("iec")
    public String getIec() {
        return iec;
    }

    @JsonProperty("iec")
    public void setIec(String iec) {
        this.iec = iec;
    }

    @JsonProperty("balance")
    public Integer getBalance() {
        return balance;
    }

    @JsonProperty("balance")
    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    @JsonProperty("bonus")
    public Integer getBonus() {
        return bonus;
    }

    @JsonProperty("bonus")
    public void setBonus(Integer bonus) {
        this.bonus = bonus;
    }

    @JsonProperty("trackers_count")
    public Integer getTrackersCount() {
        return trackersCount;
    }

    @JsonProperty("trackers_count")
    public void setTrackersCount(Integer trackersCount) {
        this.trackersCount = trackersCount;
    }

    @JsonProperty("creation_date")
    public String getCreationDate() {
        return creationDate;
    }

    @JsonProperty("creation_date")
    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    @JsonProperty("verified")
    public Boolean getVerified() {
        return verified;
    }

    @JsonProperty("verified")
    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    @JsonProperty("phone_verified")
    public Boolean getPhoneVerified() {
        return phoneVerified;
    }

    @JsonProperty("phone_verified")
    public void setPhoneVerified(Boolean phoneVerified) {
        this.phoneVerified = phoneVerified;
    }

    @Override
    public String toString() {
        return new ToStringBuilder("Customer").append("id", id).append("dealerId", dealerId).append("activated", activated).append("login", login).append("firstName", firstName).append("middleName", middleName).append("lastName", lastName).append("legalType", legalType).append("postCity", postCity).append("phone", phone).append("postCountry", postCountry).append("postIndex", postIndex).append("postRegion", postRegion).append("postStreetAddress", postStreetAddress).append("registeredCountry", registeredCountry).append("registeredIndex", registeredIndex).append("registeredRegion", registeredRegion).append("registeredCity", registeredCity).append("registeredStreetAddress", registeredStreetAddress).append("tin", tin).append("stateRegNum", stateRegNum).append("okpoCode", okpoCode).append("legalName", legalName).append("iec", iec).append("balance", balance).append("bonus", bonus).append("trackersCount", trackersCount).append("creationDate", creationDate).append("verified", verified).append("phoneVerified", phoneVerified).toString();
    }

}

