package com.nsoroma.trackermonitoring.datasourceclient.panelAPI.model; ;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "label",
        "group_id",
        "user_id",
        "source",
        "deleted",
        "dealer_id",
        "creation_date",
        "model_name",
        "clone"
})
public class Tracker {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("label")
    private String label;
    @JsonProperty("group_id")
    private Integer groupId;
    @JsonProperty("user_id")
    private Integer userId;
    @JsonProperty("source")
    private Source source;
    @JsonProperty("deleted")
    private Boolean deleted;
    @JsonProperty("dealer_id")
    private Integer dealerId;
    @JsonProperty("creation_date")
    private String creationDate;
    @JsonProperty("model_name")
    private String modelName;
    @JsonProperty("clone")
    private Boolean clone;

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("label")
    public String getLabel() {
        return label;
    }

    @JsonProperty("label")
    public void setLabel(String label) {
        this.label = label;
    }

    @JsonProperty("group_id")
    public Integer getGroupId() {
        return groupId;
    }

    @JsonProperty("group_id")
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    @JsonProperty("user_id")
    public Integer getUserId() {
        return userId;
    }

    @JsonProperty("user_id")
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @JsonProperty("source")
    public Source getSource() {
        return source;
    }

    @JsonProperty("source")
    public void setSource(Source source) {
        this.source = source;
    }

    @JsonProperty("deleted")
    public Boolean getDeleted() {
        return deleted;
    }

    @JsonProperty("deleted")
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    @JsonProperty("dealer_id")
    public Integer getDealerId() {
        return dealerId;
    }

    @JsonProperty("dealer_id")
    public void setDealerId(Integer dealerId) {
        this.dealerId = dealerId;
    }

    @JsonProperty("creation_date")
    public String getCreationDate() {
        return creationDate;
    }

    @JsonProperty("creation_date")
    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    @JsonProperty("model_name")
    public String getModelName() {
        return modelName;
    }

    @JsonProperty("model_name")
    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    @JsonProperty("clone")
    public Boolean getClone() {
        return clone;
    }

    @JsonProperty("clone")
    public void setClone(Boolean clone) {
        this.clone = clone;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("label", label).append("groupId", groupId).append("userId", userId).append("source", source).append("deleted", deleted).append("dealerId", dealerId).append("creationDate", creationDate).append("modelName", modelName).append("clone", clone).toString();
    }

}
