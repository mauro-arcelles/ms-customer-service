package com.project1.ms_customer_service.repository;

import com.project1.ms_customer_service.model.entity.BusinessCustomer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface BusinessCustomerRepository extends ReactiveMongoRepository<BusinessCustomer, String> {
    Mono<BusinessCustomer> findByRuc(String ruc);
}
