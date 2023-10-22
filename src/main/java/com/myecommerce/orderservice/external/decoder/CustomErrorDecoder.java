package com.myecommerce.orderservice.external.decoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myecommerce.orderservice.exception.CustomException;
import com.myecommerce.orderservice.external.response.ErrorResponse;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@Log4j2
public class CustomErrorDecoder implements ErrorDecoder {
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {
        log.info("::{}", response.request().url());

        try {
            ErrorResponse errorResponse = objectMapper.readValue(
                    response.body().asInputStream(),
                    ErrorResponse.class);
            return new CustomException(
                    errorResponse.getErrorCode(),
                    errorResponse.getErrorMessage(),
                    response.status());

        } catch (IOException e) {
            throw new CustomException(
                    "Internal Server Error",
                    "INTERNAL_SERVER_ERROR",
                    500);
        }
    }
}
