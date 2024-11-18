package com.musinsa.assignment.common.exception;

import com.musinsa.assignment.common.dto.ErrorResponse;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(ApiException.class)
    protected ResponseEntity<ErrorResponse> apiException(ApiException e) {
        ErrorType errorType = e.getErrorType();
        ErrorResponse errorResponse = new ErrorResponse(errorType.getStatus(), errorType.getCode(), e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorType.getStatus()));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> globalException(Exception e) {
        ErrorType errorType = ErrorType.INTERNAL_SERVER_ERROR; // default
        ErrorResponse errorResponse = new ErrorResponse(
                errorType.getStatus(), errorType.getCode(), e.getClass().getSimpleName());
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorType.getStatus()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException e){
        ErrorType errorType = ErrorType.METHOD_ARGUMENT_NOT_VALID_ERROR;
        String message = Objects.requireNonNull(e.getFieldError()).getDefaultMessage();
        ErrorResponse errorResponse = new ErrorResponse(errorType.getStatus(), errorType.getCode(), message);
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorResponse.getStatus()));
    }

    @ExceptionHandler({
            MissingServletRequestParameterException.class,
            ConstraintViolationException.class,
            IllegalStateException.class,
            IllegalArgumentException.class,
            HttpRequestMethodNotSupportedException.class
    })
    protected ResponseEntity<ErrorResponse> badRequestException(Exception e){
        ErrorType errorType = ErrorType.BAD_REQUEST_ERROR;
        String message = e.getClass().getSimpleName();
        ErrorResponse errorResponse = new ErrorResponse(errorType.getStatus(), errorType.getCode(), message);
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorResponse.getStatus()));
    }

}
