package com.project1.ms_customer_service.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.project1.ms_customer_service.model.CustomerRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.MethodParameter;
import org.springframework.core.codec.DecodingException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebInputException;
import reactor.test.StepVerifier;

@SpringBootTest
class GlobalExceptionHandlerTest {

    @Autowired
    private GlobalExceptionHandler handler;

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    void handleInvalidCustomerTypeException() {
        InvalidCustomerTypeException ex = new InvalidCustomerTypeException();

        StepVerifier.create(globalExceptionHandler.handleInvalidCustomerTypeException(ex))
            .expectNextMatches(response ->
                response.getStatusCode() == HttpStatus.BAD_REQUEST)
            .verifyComplete();
    }

    @Test
    void handleBadRequestException() {
        BadRequestException ex = new BadRequestException("Bad request");

        StepVerifier.create(globalExceptionHandler.handleBadRequestException(ex))
            .expectNextMatches(response ->
                response.getStatusCode() == HttpStatus.BAD_REQUEST)
            .verifyComplete();
    }

    @Test
    void handleServerWebInputException() {
        MethodParameter methodParameter = new MethodParameter(
            this.getClass().getDeclaredMethods()[0], -1);
        InvalidFormatException invalidFormatException = new InvalidFormatException(
            null, "Invalid format", "value", Integer.class);
        JsonMappingException.Reference ref = new JsonMappingException.Reference(null, "testField");
        invalidFormatException.prependPath(ref);

        DecodingException decodingException = new DecodingException(
            "Decoding error", invalidFormatException);

        ServerWebInputException ex = new ServerWebInputException(
            CustomerRequest.class.getName(), methodParameter, decodingException);

        StepVerifier.create(globalExceptionHandler.handleServerWebInputException(ex))
            .expectNextMatches(response ->
                response.getStatusCode() == HttpStatus.BAD_REQUEST)
            .verifyComplete();
    }
}
