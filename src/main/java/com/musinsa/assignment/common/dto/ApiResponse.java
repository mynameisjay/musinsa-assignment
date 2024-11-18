package com.musinsa.assignment.common.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponse<T> {

    private final Integer status;

    private final String message;

    private final T data;

    private ApiResponse(Integer status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static ApiResponse<?> success() {
        return new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.name(), null);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.name(), data);
    }

}
