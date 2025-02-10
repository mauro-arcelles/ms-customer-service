package com.project1.ms_customer_service.exception;

public class InvalidCustomerTypeException extends RuntimeException {
    public InvalidCustomerTypeException() {
        super("Invalid customer type. Must be PERSONAL or BUSINESS");
    }
}
