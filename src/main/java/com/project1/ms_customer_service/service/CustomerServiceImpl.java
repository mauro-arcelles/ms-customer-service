package com.project1.ms_customer_service.service;

import com.project1.ms_customer_service.exception.CustomerNotFoundException;
import com.project1.ms_customer_service.model.CustomerRequest;
import com.project1.ms_customer_service.model.CustomerResponse;
import com.project1.ms_customer_service.model.entity.Customer;
import com.project1.ms_customer_service.model.entity.CustomerMapper;
import com.project1.ms_customer_service.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public Flux<CustomerResponse> findAll() {
        return customerRepository.findAll().map(c -> customerMapper.getCustomerResponse(c));
    }

    @Override
    public Mono<CustomerResponse> findById(String id) {
        return customerRepository.findById(id)
                .map(c -> customerMapper.getCustomerResponse(c))
                .switchIfEmpty(Mono.error(new CustomerNotFoundException("Customer not found with id: " + id)));
    }

    @Override
    public Mono<CustomerResponse> create(Mono<CustomerRequest> request) {
        Mono<Customer> customerEntity = request.map(c -> customerMapper.getCustomerEntity(c, null));
        return customerEntity.flatMap(ce -> customerRepository.save(ce))
                .map(c -> customerMapper.getCustomerResponse(c));
    }

    @Override
    public Mono<CustomerResponse> update(String id, CustomerRequest request) {
        return customerRepository.findById(id)
//                .switchIfEmpty(Mono.error(new CustomerNotFoundException("Customer not found with id: " + id)))
                .flatMap(existingCustomer -> {
                    Mono<Customer> savedCustomer = customerRepository.save(customerMapper.getCustomerEntity(request, id))
                            .doOnSuccess(c -> log.info("Updated customer: {}", c.getId()));
                    return savedCustomer.map(c -> customerMapper.getCustomerResponse(c));
                });
    }

    @Override
    public Mono<Void> delete(String id) {
        return customerRepository.findById(id)
//                .switchIfEmpty(Mono.error(new CustomerNotFoundException("Customer not found with id: " + id)))
                .flatMap(customer -> customerRepository.delete(customer)
                        .doOnSuccess(v -> log.info("Deleted customer: {}", id)));
    }
}
