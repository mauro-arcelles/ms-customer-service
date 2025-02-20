package com.project1.ms_customer_service.business;

import com.project1.ms_customer_service.model.CustomerPatchRequest;
import com.project1.ms_customer_service.model.CustomerRequest;
import com.project1.ms_customer_service.model.CustomerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {
    Flux<CustomerResponse> findAll();

    Mono<CustomerResponse> findById(String id);

    Mono<CustomerResponse> create(Mono<CustomerRequest> request);

    Mono<CustomerResponse> update(String id, Mono<CustomerPatchRequest> request);

    Mono<Void> delete(String id);

    Mono<CustomerResponse> findByRuc(String ruc);

    Mono<CustomerResponse> findByDni(String dni);
}
