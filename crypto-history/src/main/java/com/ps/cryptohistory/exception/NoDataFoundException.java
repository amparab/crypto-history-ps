package com.ps.cryptohistory.exception;

import org.springframework.http.HttpStatus;

public class NoDataFoundException extends Exception{
    private String outputMessage;

    public NoDataFoundException(String outputMessage) {
        super();
        this.outputMessage = outputMessage;
    }

    public String getOutputMessage() {
        return outputMessage;
    }

    public void setOutputMessage(String outputMessage) {
        this.outputMessage = outputMessage;
    }

}