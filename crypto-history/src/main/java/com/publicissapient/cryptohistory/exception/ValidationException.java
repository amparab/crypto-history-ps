package com.publicissapient.cryptohistory.exception;

import org.springframework.http.HttpStatus;

public class ValidationException extends Exception{

    private String outputMessage;

    public ValidationException(String outputMessage, HttpStatus httpStatus) {
        super();
        this.outputMessage = outputMessage;
    }

    public ValidationException(String outputMessage) {
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
