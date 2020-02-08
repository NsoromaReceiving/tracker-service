package com.nsoroma.trackermonitoring.restcontrollers;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.nsoroma.trackermonitoring.model.customer.SlimCustomer;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2019-09-12T18:13:17.843Z")

@Api(value = "customer", description = "the customers API")
public interface CustomerApi {

    Logger log = LoggerFactory.getLogger(SchedulesApi.class);

    default Optional<ObjectMapper> getObjectMapper() {
        return Optional.empty();
    }

    default Optional<HttpServletRequest> getRequest() {
        return Optional.empty();
    }

    default Optional<String> getAcceptHeader() {
        return getRequest().map(r -> r.getHeader("Accept"));
    }

    public List<SlimCustomer> getCustomers() throws IOException, UnirestException;

    @ApiOperation(value = "get a list of customers", nickname = "getCustomers", notes = "gets a list of all customers", tags={ "developers", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success! customer list"),
            @ApiResponse(code = 406, message = "Not Acceptable") })
    @RequestMapping(value = "/api/customers",
            consumes = { "application/json" },
            method = RequestMethod.GET)
    @CrossOrigin
    default ResponseEntity<List<SlimCustomer>> Customers() {
        try {
            if(getAcceptHeader().isPresent()) {
                return new ResponseEntity<>(getCustomers(), HttpStatus.OK);
            } else {
                log.warn("ObjectMapper or HttpServletRequest not configured in default CustomerApi interface so no example is generated");
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        } catch (Exception e) {
            log.error("Unexpected error getting customers");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

