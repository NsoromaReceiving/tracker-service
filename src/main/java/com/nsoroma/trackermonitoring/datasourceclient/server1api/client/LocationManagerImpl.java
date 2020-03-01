package com.nsoroma.trackermonitoring.datasourceclient.server1api.client;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.nsoroma.trackermonitoring.datasourceclient.server1api.model.LatestLocation;
import com.nsoroma.trackermonitoring.datasourceclient.server1api.model.LatestLocationListWrapper;
import com.nsoroma.trackermonitoring.datasourceclient.server1api.model.UserSession;
import com.nsoroma.trackermonitoring.exceptions.DataSourceClientResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
public class LocationManagerImpl implements LocationManager {

    @Value("${nsoromagps.server1.api.host}")
    private String host;

    @Autowired
    private ApiAuthentication apiAuthentication;

    @Override
    public List<LatestLocation> getLatestLocation() throws UnirestException, IOException, DataSourceClientResponseException {

        UserSession userSession = apiAuthentication.getUserSession();

        String authUrl = host + "management/LocationManager.asmx/GetLatestLocationListForUser2?UserIdGuid="+ userSession.getUserIdGuid() +"&SessionId="+ userSession.getSessionId() +"&LastDateReceivedUtc=2000-02-08T06:00:00";
        String latestLocationXml = null;
        List<LatestLocation> latestLocations = Collections.emptyList();

        HttpResponse<String> response = Unirest.get(authUrl).asString();
        if (response.getStatus() == 200) {
            latestLocationXml = response.getBody();
            XmlMapper xmlMapper = new XmlMapper();
            System.out.println(latestLocationXml);
            LatestLocationListWrapper latestLocationListWrapper = xmlMapper.readValue(latestLocationXml, LatestLocationListWrapper.class);
            if (latestLocationListWrapper != null && latestLocationListWrapper.getLatestLocations() != null) {
                latestLocations = latestLocationListWrapper.getLatestLocations();
            }
        } else {
            throw new DataSourceClientResponseException(Class.class.getName(), authUrl, response.getStatus());
        }
        return latestLocations;
    }
}
