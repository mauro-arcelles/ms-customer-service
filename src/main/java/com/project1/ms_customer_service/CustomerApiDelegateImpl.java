package com.project1.ms_customer_service;

import com.project1.ms_customer_service.api.CustomersApiDelegate;
import com.project1.ms_customer_service.model.CustomerRequest;
import com.project1.ms_customer_service.model.CustomerResponse;
import com.project1.ms_customer_service.business.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Component
@Slf4j
public class CustomerApiDelegateImpl implements CustomersApiDelegate {

    @Autowired
    private CustomerService customerService;

    @Override
        public Mono<ResponseEntity<CustomerResponse>> createCustomer(@Valid Mono<CustomerRequest> customerRequest, ServerWebExchange exchange) {
        return customerService.create(customerRequest)
                .map(c -> ResponseEntity.status(HttpStatus.CREATED).body(c));
    }

    @Override
    public Mono<ResponseEntity<Void>> deleteCustomer(String id, ServerWebExchange exchange) {
        return CustomersApiDelegate.super.deleteCustomer(id, exchange);
    }

    @Override
    public Mono<ResponseEntity<CustomerResponse>> getCustomer(String id, ServerWebExchange exchange) {
        return customerService.findById(id).map(c -> ResponseEntity.ok().body(c));
    }

    @Override
    public Mono<ResponseEntity<Flux<CustomerResponse>>> getCustomers(ServerWebExchange exchange) {
        return Mono.just(ResponseEntity.ok().body(customerService.findAll()));
    }

    @Override
    public Mono<ResponseEntity<CustomerResponse>> updateCustomer(String id, Mono<CustomerRequest> customerRequest, ServerWebExchange exchange) {
        return CustomersApiDelegate.super.updateCustomer(id, customerRequest, exchange);
    }
}
