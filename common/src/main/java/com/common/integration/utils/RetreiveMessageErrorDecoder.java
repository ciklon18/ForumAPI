package com.common.integration.utils;


import com.common.exception.CustomException;
import com.common.exception.ExceptionType;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Request;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@RequiredArgsConstructor
public class RetreiveMessageErrorDecoder implements ErrorDecoder {
    private final ObjectMapper mapper;

    private final Map<Integer, ExceptionType> REQUEST_STATUS_CODES = Map.of(
            HttpStatus.BAD_REQUEST.value(), ExceptionType.BAD_REQUEST,
            HttpStatus.NOT_FOUND.value(), ExceptionType.NOT_FOUND,
            HttpStatus.INTERNAL_SERVER_ERROR.value(), ExceptionType.FATAL,
            HttpStatus.UNAUTHORIZED.value(), ExceptionType.UNAUTHORIZED,
            HttpStatus.FORBIDDEN.value(), ExceptionType.FORBIDDEN,
            HttpStatus.CONFLICT.value(), ExceptionType.ALREADY_EXISTS,
            HttpStatus.METHOD_NOT_ALLOWED.value(), ExceptionType.ILLEGAL
    );

    @Override
    public Exception decode(String methodKey, Response response) {
        String errorMessage = null;
        ExceptionType errorCode = REQUEST_STATUS_CODES.getOrDefault(response.status(), ExceptionType.FATAL);
        if (response.request().httpMethod() != Request.HttpMethod.HEAD) {
            errorMessage = getErrorBody(response);
        }
        return new CustomException(
                errorCode,
                errorMessage
        );
    }


    private String getErrorBody(Response response)  {
        if (response.body() == null) {
            return null;
        }
        try (InputStream bodyIs = response.body().asInputStream()) {
            return mapper.readValue(bodyIs, String.class);
        } catch (IOException e) {
            throw new CustomException(
                    ExceptionType.FATAL,
                    "IOException. Error processing ErrorMessage from microservice"
            );
        } catch (Exception e) {
            throw new CustomException(
                    ExceptionType.FATAL,
                    "Exception. Error processing ErrorMessage from microservice"
            );
        }
    }
}

