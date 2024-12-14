package com.ps.cryptohistory.exception;

import org.springframework.http.HttpStatus;

public class CryptoHistoryException extends Exception {
    private String outputMessage;

    public CryptoHistoryException(String outputMessage) {
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