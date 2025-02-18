package com.project1.ms_customer_service;

import com.project1.ms_customer_service.api.CustomersApiDelegate;
import com.project1.ms_customer_service.model.CustomerPatchRequest;
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
    public Mono<ResponseEntity<CustomerResponse>> createCustomer(Mono<CustomerRequest> customerRequest, ServerWebExchange exchange) {
        return customerService.create(customerRequest)
                .map(ResponseEntity.status(HttpStatus.CREATED)::body);
    }

    @Override
    public Mono<ResponseEntity<Void>> deleteCustomer(String id, ServerWebExchange exchange) {
        return customerService.delete(id).map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<CustomerResponse>> getCustomer(String id, ServerWebExchange exchange) {
        return customerService.findById(id).map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Flux<CustomerResponse>>> getCustomers(ServerWebExchange exchange) {
        return Mono.just(ResponseEntity.ok().body(customerService.findAll()));
    }

    @Override
    public Mono<ResponseEntity<CustomerResponse>> updateCustomer(String id, Mono<CustomerPatchRequest> customerPatchRequest, ServerWebExchange exchange) {
        return customerService.update(id, customerPatchRequest).map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<CustomerResponse>> getCustomerByDni(String dni, ServerWebExchange exchange) {
        return customerService.findByDni(dni).map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<CustomerResponse>> getCustomerByRuc(String ruc, ServerWebExchange exchange) {
        return customerService.findByRuc(ruc).map(ResponseEntity::ok);
    }
}
