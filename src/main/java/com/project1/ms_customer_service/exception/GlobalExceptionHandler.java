package com.project1.ms_customer_service.exception;

import com.fasterxml.jackson.databind.exc.InvalidTypeIdException;
import com.project1.ms_customer_service.model.ResponseBase;
import org.springframework.core.codec.DecodingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<Map<String, List<String>>>> handleValidationErrors(WebExchangeBindException ex) {
        Map<String, List<String>> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())
                ));
        return Mono.just(ResponseEntity.badRequest().body(errors));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<String>> handleGenericError(Exception ex) {
        ex.printStackTrace();
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Internal Server Error rrrr"));
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public Mono<ResponseEntity<ResponseBase>> handleCustomerNotFoundException(Exception ex) {
        ResponseBase responseBase = new ResponseBase();
        responseBase.setMessage(ex.getMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(responseBase));
    }

    @ExceptionHandler(InvalidCustomerTypeException.class)
    public Mono<ResponseEntity<ResponseBase>> handleInvalidCustomerTypeException(Exception ex) {
        ResponseBase responseBase = new ResponseBase();
        responseBase.setMessage(ex.getMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(responseBase));
    }

    @ExceptionHandler(BadRequestException.class)
    public Mono<ResponseEntity<ResponseBase>> handleBadRequestException(Exception ex) {
        ResponseBase responseBase = new ResponseBase();
        responseBase.setMessage(ex.getMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(responseBase));
    }

    @ExceptionHandler(ServerWebInputException.class)
    public Mono<ResponseEntity<ResponseBase>> handleServerWebInputException(ServerWebInputException ex) {
        ResponseBase responseBase = new ResponseBase();

        if (ex.getCause() instanceof DecodingException &&
                ex.getCause().getCause() instanceof InvalidTypeIdException) {
            responseBase.setMessage("Invalid customer type. Must be PERSONAL or BUSINESS");
        } else {
            responseBase.setMessage("Invalid request format");
        }

        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(responseBase));
    }
}