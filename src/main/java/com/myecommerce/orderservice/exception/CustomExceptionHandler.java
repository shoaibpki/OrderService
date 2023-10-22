package com.myecommerce.orderservice.exception;

import com.myecommerce.orderservice.external.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    ResponseEntity<ErrorResponse> RestCustomExceptionHandler(CustomException exception){
        return new ResponseEntity<>(
                new ErrorResponse().builder()
                        .errorMessage(exception.getMessage())
                        .errorCode(exception.getErrorCode())
                        .build(),
                HttpStatus.valueOf(exception.getStatus()));
    }
}
