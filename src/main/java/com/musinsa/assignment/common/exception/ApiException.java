package com.musinsa.assignment.common.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {

    private final ErrorType errorType;


    public ApiException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }

    public ApiException(ErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
    }

}
