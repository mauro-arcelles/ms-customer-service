package com.project1.ms_customer_service.service;

import com.project1.ms_customer_service.model.CustomerRequest;
import com.project1.ms_customer_service.model.CustomerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {
    Flux<CustomerResponse> findAll();
    Mono<CustomerResponse> findById(String id);
    Mono<CustomerResponse> create(Mono<CustomerRequest> customer);
    Mono<CustomerResponse> update(String id, CustomerRequest customer);
    Mono<Void> delete(String id);
}
