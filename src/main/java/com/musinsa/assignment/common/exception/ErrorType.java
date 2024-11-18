package com.musinsa.assignment.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorType {

    /** Business Errors */
    UNKNOWN_ERROR(HttpStatus.BAD_REQUEST.value(), "BE000",  "UNKNOWN_ERROR"),

    /** Global Errors */
    CONFLICT_ERROR(HttpStatus.CONFLICT.value(), "GE004",  "CONFLICT_ERROR"),
    NOT_FOUND_ERROR(HttpStatus.NOT_FOUND.value(), "GE003",  "NOT_FOUND_ERROR"),
    BAD_REQUEST_ERROR(HttpStatus.BAD_REQUEST.value(), "GE002",  "BAD_REQUEST_ERROR"),
    METHOD_ARGUMENT_NOT_VALID_ERROR(HttpStatus.BAD_REQUEST.value(), "GE001",  "METHOD_ARGUMENT_NOT_VALID_ERROR"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "GE000", "INTERNAL_SERVER_ERROR");


    private final Integer status;
    private final String code;
    private final String message;

    ErrorType(Integer status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

}
