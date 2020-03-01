package com.nsoroma.trackermonitoring.exceptions;

public class DataSourceClientResponseException extends Exception {
    public DataSourceClientResponseException(String className, String url, Integer statusCode) {
        super(className + ": 200 response from data source client failed: " + url + "status code: " + statusCode);
    }
}
