package com.project1.ms_customer_service.repository;

import com.project1.ms_customer_service.model.entity.Customer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CustomerRepository extends ReactiveMongoRepository<Customer, String> {
    Mono<Customer> findByDocumentNumber(String documentNumber);
    Mono<Boolean> existsByDocumentNumber(String documentNumber);
}