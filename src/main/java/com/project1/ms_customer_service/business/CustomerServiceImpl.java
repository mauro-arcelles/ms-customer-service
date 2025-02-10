package com.project1.ms_customer_service.business;

import com.project1.ms_customer_service.exception.CustomerNotFoundException;
import com.project1.ms_customer_service.exception.InvalidCustomerTypeException;
import com.project1.ms_customer_service.model.CustomerPatchRequest;
import com.project1.ms_customer_service.model.CustomerRequest;
import com.project1.ms_customer_service.model.CustomerResponse;
import com.project1.ms_customer_service.model.entity.CustomerType;
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
        return customerRepository.findAll()
                .map(customerMapper::getCustomerResponse);
    }

    @Override
    public Mono<CustomerResponse> findById(String id) {
        return customerRepository.findById(id)
                .map(customerMapper::getCustomerResponse)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException("Customer not found with id: " + id)));
    }

    @Override
    public Mono<CustomerResponse> create(Mono<CustomerRequest> request) {
        return request
                .filter(req -> isValidCustomerType(req.getType()))
                .switchIfEmpty(Mono.error(new InvalidCustomerTypeException()))
                .map(req -> customerMapper.getCustomerEntity(req, null))
                .flatMap(customerRepository::save)
                .map(customerMapper::getCustomerResponse);
    }

    @Override
    public Mono<CustomerResponse> update(String id, Mono<CustomerPatchRequest> request) {
        return customerRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException("Customer not found with id: " + id)))
                .flatMap(existingCustomer -> request
                        .flatMap(req -> customerRepository.save(customerMapper.getCustomerUpdateEntity(req, existingCustomer)))
                        .doOnSuccess(c -> log.info("Updated customer: {}", c.getId()))
                        .map(customerMapper::getCustomerResponse)
                );
    }

    @Override
    public Mono<Void> delete(String id) {
        return customerRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException("Customer not found with id: " + id)))
                .flatMap(customer -> customerRepository.delete(customer))
                .doOnSuccess(v -> log.info("Deleted customer: {}", id));
    }

    private boolean isValidCustomerType(String customerType) {
        try {
            CustomerType.valueOf(customerType);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
