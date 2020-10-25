package com.nsoroma.trackermonitoring.datasourceclient.server1api.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter @NoArgsConstructor
public class UserSession implements Serializable {

    @JsonProperty("WebserviceUri")
    private String WebserviceUri;
    @JsonProperty("UserIdGuid")
    private String UserIdGuid;
    @JsonProperty("SessionId")
    private String SessionId;
    @JsonProperty("Uri")
    private String Uri;

}
