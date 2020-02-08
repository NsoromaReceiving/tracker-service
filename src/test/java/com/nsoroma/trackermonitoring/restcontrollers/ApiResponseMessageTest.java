package com.nsoroma.trackermonitoring.restcontrollers;

import org.junit.Test;

import static org.junit.Assert.*;

public class ApiResponseMessageTest {

    private ApiResponseMessage apiResponseMessage;

    @Test
    public void testErrorCode(){
        apiResponseMessage = new ApiResponseMessage(1,"errorMessage");
        assertEquals("error", apiResponseMessage.getType());
        assertEquals("errorMessage", apiResponseMessage.getMessage());
    }

    @Test
    public void testWarningCode(){
        apiResponseMessage = new ApiResponseMessage(2,"warningMessage");
        assertEquals("warning", apiResponseMessage.getType());
    }

    @Test
    public void testInfoCode(){
        apiResponseMessage = new ApiResponseMessage(3,"infoMessage");
        assertEquals("info", apiResponseMessage.getType());
    }

    @Test
    public void testOkCode(){
        apiResponseMessage = new ApiResponseMessage(4,"okMessage");
        assertEquals("ok", apiResponseMessage.getType());
    }

    @Test
    public void testTooBusyCode(){
        apiResponseMessage = new ApiResponseMessage(5,"tooBusyMessage");
        assertEquals("too busy", apiResponseMessage.getType());
    }

    @Test
    public void testUnknownCode(){
        apiResponseMessage = new ApiResponseMessage(6,"unknownMessage");
        assertEquals("unknown", apiResponseMessage.getType());
    }

    @Test
    public void getErrorCode(){
        apiResponseMessage = new ApiResponseMessage();
        apiResponseMessage.setCode(2);
        assertEquals(2, apiResponseMessage.getCode());
    }

    @Test
    public void getErrorMessage() {
        apiResponseMessage = new ApiResponseMessage();
        apiResponseMessage.setMessage("testErrorMessage");
        assertEquals("testErrorMessage", apiResponseMessage.getMessage());
    }

}