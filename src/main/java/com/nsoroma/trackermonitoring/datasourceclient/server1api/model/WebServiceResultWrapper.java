package com.nsoroma.trackermonitoring.datasourceclient.server1api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
public class WebServiceResultWrapper implements Serializable {

    @JsonProperty("Status")
    private String status;

    @JsonProperty("ErrorCode")
    private String errorCode;

    @JsonProperty("ErrorDescription")
    private String errorDescription;

    @JsonProperty("WebserviceContent")
    private WebServiceContentWrapper webServiceContent;

    @Override
    public String toString() {
        return "WebserviceContentWrapper [status=" + status + ", errorCode=" + errorCode + ", errorDescription="
                + errorDescription + ", webServiceContent=" + webServiceContent + "]";
    }

}
