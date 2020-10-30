package com.nsoroma.trackermonitoring.datasourceclient.server1api.client;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.nsoroma.trackermonitoring.datasourceclient.server1api.model.*;
import com.nsoroma.trackermonitoring.exceptions.DataSourceClientResponseException;
import com.nsoroma.trackermonitoring.services.Trackers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UnitManagerImpl implements UnitManager {

    @Value("${nsoromagps.server1.api.host}")
    private String host;

    @Autowired
    private ApiAuthentication apiAuthentication;

    private Logger log = LoggerFactory.getLogger(UnitManagerImpl.class);

    @Override
    public List<Unit> getUnits(List<String> uids) throws DataSourceClientResponseException, UnirestException, IOException {

        UserSession userSession = apiAuthentication.getUserSession();
        ArrayList<Unit> units = new ArrayList<>();

        int counter = 0;
        for (String ids: uids) {
            counter += 1;
            String unitsUrl = host + "management/DistributorManager.asmx/UnitList?UserIdGuid="+ userSession.getUserIdGuid() +"&SessionId="+ userSession.getSessionId() +
                    "&Units="+ids+"&OptionsJSON=";
            String unitListXml = null;


            HttpResponse<String> response = Unirest.get(unitsUrl).asString();
            if (response.getStatus() == 200) {
                unitListXml = response.getBody();
                XmlMapper xmlMapper = new XmlMapper();
                WebServiceResultWrapper webServiceResultWrapper = xmlMapper.readValue(unitListXml, WebServiceResultWrapper.class);
                if (webServiceResultWrapper != null && webServiceResultWrapper.getWebServiceContent() != null &&
                        webServiceResultWrapper.getWebServiceContent().getData() != null  && webServiceResultWrapper.getWebServiceContent().getData().getUnits() != null) {
                    units.addAll(webServiceResultWrapper.getWebServiceContent().getData().getUnits());
                }
            } else {
                throw new DataSourceClientResponseException(Class.class.getName(), unitsUrl, response.getStatus());
            }
            log.info("get units");
            log.info(String.valueOf(counter));
        }

        log.info(String.valueOf(units));
        return units;
    }

    @Override
    public List<LatestLocation> getLatestLocation(List<String> uids) throws UnirestException, IOException, DataSourceClientResponseException {
        UserSession userSession = apiAuthentication.getUserSession();

        String latestLocationXml = null;
        ArrayList<LatestLocation> latestLocations = new ArrayList<>();

        int counter = 0;
        for (String ids: uids) {
            counter += 1;
            log.info("checking the legnth of uid for location stuff");
            log.info(String.valueOf(uids.size()));
            String latestLocation = host + "management/DistributorManager.asmx/UnitLatestLocations?UserIdGuid="+ userSession.getUserIdGuid() +"&SessionId="+ userSession.getSessionId() +"&LastDateReceivedUtc="+
                    "&Units="+ids+"&OptionsJSON=D";
            log.info(latestLocation);
            HttpResponse<String> response = Unirest.get(latestLocation).asString();
            if (response.getStatus() == 200) {
                latestLocationXml = response.getBody();
                XmlMapper xmlMapper = new XmlMapper();
                WebServiceResultWrapper latestLocationListWrapper = xmlMapper.readValue(latestLocationXml, WebServiceResultWrapper.class);
                if (latestLocationListWrapper != null && latestLocationListWrapper.getWebServiceContent().getData().getLatestLocations() != null) {
                    log.info("locationsss");
                    List<LatestLocation> latestLocations1 =  latestLocationListWrapper.getWebServiceContent().getData().getLatestLocations();
                    log.info(String.valueOf(latestLocations1.size()));
                    latestLocations.addAll(latestLocations1);
                    log.info("getting location");
                    log.info(String.valueOf(counter));
                }
            } else {
                throw new DataSourceClientResponseException(Class.class.getName(), latestLocation, response.getStatus());
            }

        }
        log.info(String.valueOf(latestLocations.size()));
        return latestLocations;
    }


    @Override
    public ArrayList<String> getUnitsStringChunks() throws DataSourceClientResponseException, UnirestException, IOException {

        UserSession userSession = apiAuthentication.getUserSession();

        String authUrl = host + "management/UnitManager.asmx/UnitCustomDetails?UserIdGuid="+ userSession.getUserIdGuid() +"&SessionId="+ userSession.getSessionId();
        String unitListXml = null;

        ArrayList<String> unitsStringList = new ArrayList<>();

        HttpResponse<String> response = Unirest.get(authUrl).asString();
        if (response.getStatus() == 200) {
            unitListXml = response.getBody();
            XmlMapper xmlMapper = new XmlMapper();
            WebServiceResultWrapper webServiceResultWrapper = xmlMapper.readValue(unitListXml, WebServiceResultWrapper.class);
            if (webServiceResultWrapper != null && webServiceResultWrapper.getWebServiceContent().getData().getUnits() != null) {
                List<Unit> unitList = webServiceResultWrapper.getWebServiceContent().getData().getUnits();
                log.info("total number of tracker...");
                log.info(String.valueOf(unitList.size()));
                log.info("total number of units are active...");
                log.info(String.valueOf((int) unitList.stream().filter(unit -> unit.getStatus().equalsIgnoreCase("Active")).count()));

                ArrayList<String> uidList = (ArrayList<String>) unitList.stream().map(Unit::getImei).collect(Collectors.toList()).parallelStream().filter(Objects::nonNull).collect(Collectors.toList());
                uidList.removeAll(Collections.singleton(""));
                int stringLength = uidList.size();
                int start = 0;
                int sizeCovered = 0;

                while(start < stringLength) {
                    int end = Math.min(start + 100, uidList.size());

                    List<String> subStringList = uidList.subList(sizeCovered, end);
                    sizeCovered += subStringList.size();
                    String unitsString = String.join(",", subStringList);
                    unitsStringList.add(unitsString);
                    start += 100;
                }
                log.info(String.valueOf(uidList));

            }
        } else {
            throw new DataSourceClientResponseException(Class.class.getName(), authUrl, response.getStatus());
        }

        log.info(String.valueOf(unitsStringList));
        return unitsStringList;
    }


    @Override
    public List<String> getResourceImeis() throws IOException {
        Class clazz = UnitManagerImpl.class;
        InputStream inputStream = clazz.getResourceAsStream("/IMEIS.txt");
        String data = readFromInputStream(inputStream);
        return Arrays.asList(data.split("&"));
    }

    private String readFromInputStream(InputStream inputStream)
            throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            int limit = 0;
            while ((line = br.readLine()) != null) {
                limit +=1;
                resultStringBuilder.append(line).append(",");
                if (limit == 100) {
                    resultStringBuilder.append("&");
                    limit = 0;
                }
            }
        }
        return resultStringBuilder.toString();
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setApiAuthentication(ApiAuthentication apiAuthentication) {
        this.apiAuthentication = apiAuthentication;
    }
}
