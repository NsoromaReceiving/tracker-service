package com.nsoroma.trackermonitoring.datasourceclient.server1api.client;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.nsoroma.trackermonitoring.datasourceclient.server1api.model.Unit;
import com.nsoroma.trackermonitoring.datasourceclient.server1api.model.UserSession;
import com.nsoroma.trackermonitoring.datasourceclient.server1api.model.WebServiceResultWrapper;
import com.nsoroma.trackermonitoring.exceptions.DataSourceClientResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
public class UnitManagerImpl implements UnitManager {

    @Value("${nsoromagps.server1.api.host}")
    private String host;

    @Autowired
    private ApiAuthentication apiAuthentication;

    @Override
    public List<Unit> getUnits() throws DataSourceClientResponseException, UnirestException, IOException {

        UserSession userSession = apiAuthentication.getUserSession();

        String authUrl = host + "management/UnitManager.asmx/UnitCustomDetails?UserIdGuid="+ userSession.getUserIdGuid() +"&SessionId="+ userSession.getSessionId();
        String unitListXml = null;
        List<Unit> units = Collections.emptyList();

        HttpResponse<String> response = Unirest.get(authUrl).asString();
        if (response.getStatus() == 200) {
            unitListXml = response.getBody();
            XmlMapper xmlMapper = new XmlMapper();
            WebServiceResultWrapper webServiceResultWrapper = xmlMapper.readValue(unitListXml, WebServiceResultWrapper.class);
            if (webServiceResultWrapper != null && webServiceResultWrapper.getWebServiceContent().getData().getUnits() != null) {
                units = webServiceResultWrapper.getWebServiceContent().getData().getUnits();
            }
        } else {
            throw new DataSourceClientResponseException(Class.class.getName(), authUrl, response.getStatus());
        }
        return units;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setApiAuthentication(ApiAuthentication apiAuthentication) {
        this.apiAuthentication = apiAuthentication;
    }
}
