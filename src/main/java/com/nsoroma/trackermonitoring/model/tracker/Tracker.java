
package com.nsoroma.trackermonitoring.model.tracker;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "label",
    "group_id",
    "source",
    "tag_bindings",
    "clone"
})
public class Tracker {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("label")
    private String label;
    @JsonProperty("group_id")
    private Integer groupId;
    @JsonProperty("source")
    private Source source;
    @JsonProperty("tag_bindings")
    private List<Object> tagBindings = null;
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

    @JsonProperty("source")
    public Source getSource() {
        return source;
    }

    @JsonProperty("source")
    public void setSource(Source source) {
        this.source = source;
    }

    @JsonProperty("tag_bindings")
    public List<Object> getTagBindings() {
        return tagBindings;
    }

    @JsonProperty("tag_bindings")
    public void setTagBindings(List<Object> tagBindings) {
        this.tagBindings = tagBindings;
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
        return new ToStringBuilder(this).append("id", id).append("label", label).append("groupId", groupId).append("source", source).append("tagBindings", tagBindings).append("clone", clone).toString();
    }

}
