package com.api.user.exception;

public class ApiServiceException extends Exception{
    private static final long serialVersionUID = 5993077978826827583L;
    public ApiServiceException(String message) {
        super(message);
    }
}
