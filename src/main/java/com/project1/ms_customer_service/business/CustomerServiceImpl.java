package com.project1.ms_customer_service.business;

import com.project1.ms_customer_service.exception.BadRequestException;
import com.project1.ms_customer_service.exception.CustomerNotFoundException;
import com.project1.ms_customer_service.exception.InvalidCustomerTypeException;
import com.project1.ms_customer_service.model.CustomerPatchRequest;
import com.project1.ms_customer_service.model.CustomerRequest;
import com.project1.ms_customer_service.model.CustomerResponse;
import com.project1.ms_customer_service.model.PersonalCustomer;
import com.project1.ms_customer_service.model.BusinessCustomer;
import com.project1.ms_customer_service.model.entity.CustomerStatus;
import com.project1.ms_customer_service.model.entity.CustomerType;
import com.project1.ms_customer_service.repository.BusinessCustomerRepository;
import com.project1.ms_customer_service.repository.CustomerRepository;
import com.project1.ms_customer_service.repository.PersonalCustomerRepository;
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

    @Autowired
    private BusinessCustomerRepository businessCustomerRepository;

    @Autowired
    private PersonalCustomerRepository personalCustomerRepository;

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
                .filter(this::isValidCustomerType)
                .switchIfEmpty(Mono.error(new InvalidCustomerTypeException()))
                .flatMap(this::validateCustomerExists)
                .map(customerMapper::getCustomerEntity)
                .flatMap(customerRepository::save)
                .map(customerMapper::getCustomerResponse);
    }

    /**
     * Validates if a customer already exists based on their type
     * @param customer The customer request to validate
     * @return A Mono of CustomerRequest if validation passes
     * @throws BadRequestException if a PERSONAL customer with same document number exists
     * @throws BadRequestException if a BUSINESS customer with same RUC exists
     */
    private Mono<CustomerRequest> validateCustomerExists(CustomerRequest customer) {
        if (customer.getType().equals(CustomerType.PERSONAL.toString())) {
            PersonalCustomer personalCustomer = (PersonalCustomer) customer;
            return personalCustomerRepository.findByDocumentNumber(personalCustomer.getDocumentNumber())
                    .flatMap(pc -> Mono.error(new BadRequestException("PERSONAL customer already exists with document number: " + personalCustomer.getDocumentNumber())))
                    .then(Mono.just(customer));
        } else {
            BusinessCustomer businessCustomer = (BusinessCustomer) customer;
            return businessCustomerRepository.findByRuc(businessCustomer.getRuc())
                    .flatMap(bc -> Mono.error(new BadRequestException("BUSINESS customer already exists with ruc: " + businessCustomer.getRuc())))
                    .then(Mono.just(customer));
        }
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
                .flatMap(customer -> {
                    customer.setStatus(CustomerStatus.INACTIVE);
                    return customerRepository.save(customer);
                })
                .then()
                .doOnSuccess(v -> log.info("Deleted customer: {}", id));
    }

    @Override
    public Mono<CustomerResponse> findByRuc(String ruc) {
        return businessCustomerRepository.findByRuc(ruc)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException("Customer not found with ruc: " + ruc)))
                .map(customerMapper::getCustomerResponse);
    }

    @Override
    public Mono<CustomerResponse> findByDni(String dni) {
        return personalCustomerRepository.findByDocumentNumber(dni)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException("Customer not found with dni: " + dni)))
                .map(customerMapper::getCustomerResponse);
    }

    /**
     * Validates if the customer type is a valid enum value
     * @param request The customer request containing the type to validate
     * @return true if type is valid, false otherwise
     * @see CustomerType
     */
    private boolean isValidCustomerType(CustomerRequest request) {
        try {
            CustomerType.valueOf(request.getType());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
