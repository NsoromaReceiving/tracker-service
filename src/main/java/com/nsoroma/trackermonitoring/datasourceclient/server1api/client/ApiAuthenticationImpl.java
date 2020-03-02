package com.nsoroma.trackermonitoring.datasourceclient.server1api.client;

import com.mashape.unirest.http.HttpResponse;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.nsoroma.trackermonitoring.datasourceclient.server1api.model.UserSession;
import com.nsoroma.trackermonitoring.exceptions.DataSourceClientResponseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ApiAuthenticationImpl implements ApiAuthentication {

    @Value("${nsoromagps.server1.api.host}")
    private String host;

    @Value("${nsoromagps.server1.api.username}")
    private String username;

    @Value("${nsoromagps.server1.api.password}")
    private String password;

    @Override
    public UserSession getUserSession() throws UnirestException, IOException, DataSourceClientResponseException {

        String authUrl = host + "management/Authenticationmanager.asmx/UserAuthenticate?UserName=" + username + "&Password=" + password;
        String userSessionXml = null;
        UserSession userSession;
        HttpResponse<String> response = Unirest.get(authUrl).asString();
        if (response.getStatus() == 200) {
            userSessionXml = response.getBody();
            XmlMapper xmlMapper = new XmlMapper();
            userSession = xmlMapper.readValue(userSessionXml, UserSession.class);
        } else {
            throw new DataSourceClientResponseException(Class.class.getName(), authUrl, response.getStatus());
        }
        return userSession;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
