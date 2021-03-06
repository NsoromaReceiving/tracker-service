package com.nsoroma.trackermonitoring.restcontrollers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.nsoroma.trackermonitoring.exceptions.DataSourceClientResponseException;
import com.nsoroma.trackermonitoring.model.trackerstate.TrackerState;
import com.nsoroma.trackermonitoring.services.Trackers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Optional;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2019-08-28T15:39:11.130Z")

@Controller
public class TrackersApiController implements TrackersApi {
    @Autowired
    Trackers trackers;

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public TrackersApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @Override
    public Optional<ObjectMapper> getObjectMapper() {
        return Optional.ofNullable(objectMapper);
    }

    @Override
    public Optional<HttpServletRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    public LinkedHashSet<TrackerState> getTrackers(Optional<String> startDate, Optional<String> endDate, Optional<String> customerId,
                                                                      Optional<String> type, Optional<String> order, Optional<String> status, Optional<String> server) throws IOException, UnirestException {
        return trackers.getTrackers(startDate, endDate, customerId, type, order, status, server);
    }


}
