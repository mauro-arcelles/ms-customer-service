package com.project1.ms_customer_service.repository;

import com.project1.ms_customer_service.model.entity.PersonalCustomer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface PersonalCustomerRepository extends ReactiveMongoRepository<PersonalCustomer, String> {
    Mono<PersonalCustomer> findByDocumentNumber(String documentNumber);
}