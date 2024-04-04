package com.common.integration.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;

import java.io.IOException;
import java.io.InputStream;

@RequiredArgsConstructor
public class RetreiveMessageErrorDecoder implements ErrorDecoder {
    private final ObjectMapper mapper;


    @Override
    public Exception decode(String methodKey, Response response) {

        return new ServiceException(getErrorBody(response));
    }


    private String getErrorBody(Response response)  {
        try (InputStream bodyIs = response.body().asInputStream()) {
            return new String(bodyIs.readAllBytes());
        } catch (IOException e) {
            throw new ServiceException(
                    "Error processing ErrorMessage from microservice",
                    e
            );
        }
    }
}

