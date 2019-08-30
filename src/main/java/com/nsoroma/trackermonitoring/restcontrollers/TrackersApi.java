/**
 * NOTE: This class is auto generated by the swagger code generator program (2.4.7).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package com.nsoroma.trackermonitoring.restcontrollers;

import com.nsoroma.trackermonitoring.model.trackerstate.TrackerState;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2019-08-28T15:39:11.130Z")

@Api(value = "trackers", description = "the trackers API")
public interface TrackersApi {

    Logger log = LoggerFactory.getLogger(TrackersApi.class);

    default Optional<ObjectMapper> getObjectMapper() {
        return Optional.empty();
    }

    default Optional<HttpServletRequest> getRequest() {
        return Optional.empty();
    }

    default Optional<String> getAcceptHeader() {
        return getRequest().map(r -> r.getHeader("Accept"));
    }

    public LinkedHashSet<TrackerState> getTrackers(Optional<String> duration, Optional<String> customerId, Optional<String> type, Optional<String> order );

    @ApiOperation(value = "gets all trackers that meets the criteria set by the list of parameters.", nickname = "trackers", notes = "This provides a list of all trackers and thier data in a descending order of last update time.", response = TrackerState.class, responseContainer = "List", tags={ "developers", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "success!", response = TrackerState.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "bad input parameter") })
    @RequestMapping(value = "/trackers",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.GET)
    default ResponseEntity<LinkedHashSet<TrackerState>> trackers(@ApiParam(value = "'pass the time period to filter by. period format can be 2hr (2 hours) or 2dy (2 days)' ") @Valid @RequestParam(value = "duration", required = false) Optional<String> duration, @ApiParam(value = "pass in the customer Id of a particular customer to fetch trackers registered to that customer.") @Valid @RequestParam(value = "customerId", required = false) Optional<String> customerId, @ApiParam(value = "pass the tracker type to fecth trackers of a particular type") @Valid @RequestParam(value = "type", required = false) Optional<String> type, @ApiParam(value = "if set displays the fetched trackers in order of ascending or descending order of last communiction to the server. Order of ascending signified by (asc) and order of descending signified by (dsc).") @Valid @RequestParam(value = "order", required = false) Optional<String> order) {
        if(getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
            if (getAcceptHeader().get().contains("application/json")) {
                LinkedHashSet<TrackerState> trackerStates;

                trackerStates = getTrackers(duration,customerId,type,order);
                return new ResponseEntity<>(trackerStates, HttpStatus.OK);
            }
        } else {
            log.warn("ObjectMapper or HttpServletRequest not configured in default TrackersApi interface so no example is generated");
        }
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

}