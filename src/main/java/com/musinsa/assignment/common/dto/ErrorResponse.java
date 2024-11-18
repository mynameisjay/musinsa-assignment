package com.musinsa.assignment.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {

    private Integer status;

    private String errorCode;

    private String message;


    public ErrorResponse(Integer status, String code, String message) {
        this.status = status;
        this.errorCode = code;
        this.message = message;
    }

}
