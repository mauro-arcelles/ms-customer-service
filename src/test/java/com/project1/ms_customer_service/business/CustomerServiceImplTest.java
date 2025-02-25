package com.project1.ms_customer_service.business;

import com.project1.ms_customer_service.exception.BadRequestException;
import com.project1.ms_customer_service.exception.InvalidCustomerTypeException;
import com.project1.ms_customer_service.exception.NotFoundException;
import com.project1.ms_customer_service.model.CustomerPatchRequest;
import com.project1.ms_customer_service.model.CustomerRequest;
import com.project1.ms_customer_service.model.CustomerResponse;
import com.project1.ms_customer_service.model.entity.*;
import com.project1.ms_customer_service.repository.BusinessCustomerRepository;
import com.project1.ms_customer_service.repository.CustomerRepository;
import com.project1.ms_customer_service.repository.PersonalCustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class CustomerServiceImplTest {

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private BusinessCustomerRepository businessCustomerRepository;

    @MockBean
    private PersonalCustomerRepository personalCustomerRepository;

    @MockBean
    private CustomerMapper customerMapper;

    @Autowired
    private CustomerServiceImpl customerService;

    @Test
    void findAll_ShouldReturnCustomers() {
        Customer customer = new Customer();
        CustomerResponse response = new CustomerResponse();

        when(customerRepository.findAll()).thenReturn(Flux.just(customer));
        when(customerMapper.getCustomerResponse(customer)).thenReturn(response);

        StepVerifier.create(customerService.findAll())
            .expectNext(response)
            .verifyComplete();
    }

    @Test
    void findById_WhenExists_ShouldReturnCustomer() {
        String id = "1";
        Customer customer = new Customer();
        CustomerResponse response = new CustomerResponse();

        when(customerRepository.findById(id)).thenReturn(Mono.just(customer));
        when(customerMapper.getCustomerResponse(customer)).thenReturn(response);

        StepVerifier.create(customerService.findById(id))
            .expectNext(response)
            .verifyComplete();
    }

    @Test
    void findById_WhenNotExists_ShouldThrowException() {
        String id = "1";
        when(customerRepository.findById(id)).thenReturn(Mono.empty());

        StepVerifier.create(customerService.findById(id))
            .expectError(NotFoundException.class)
            .verify();
    }

    @Test
    void create_WithValidPersonalCustomer_ShouldSucceed() {
        com.project1.ms_customer_service.model.PersonalCustomer request = new com.project1.ms_customer_service.model.PersonalCustomer();
        request.setType(CustomerType.PERSONAL.toString());
        request.setDocumentNumber("123");

        Customer customer = new Customer();
        CustomerResponse response = new CustomerResponse();

        when(personalCustomerRepository.findByDocumentNumber("123")).thenReturn(Mono.empty());
        when(customerMapper.getCustomerEntity(request)).thenReturn(customer);
        when(customerRepository.save(customer)).thenReturn(Mono.just(customer));
        when(customerMapper.getCustomerResponse(customer)).thenReturn(response);

        StepVerifier.create(customerService.create(Mono.just(request)))
            .expectNext(response)
            .verifyComplete();
    }

    @Test
    void delete_ShouldSetStatusInactive() {
        String id = "1";
        Customer customer = new Customer();
        customer.setId(id);

        when(customerRepository.findById(id)).thenReturn(Mono.just(customer));
        when(customerRepository.save(any())).thenReturn(Mono.just(customer));

        StepVerifier.create(customerService.delete(id))
            .verifyComplete();

        assertEquals(CustomerStatus.INACTIVE, customer.getStatus());
    }

    @Test
    void findByRuc_WhenExists_ShouldReturnCustomer() {
        String ruc = "12345";
        BusinessCustomer customer = new BusinessCustomer();
        CustomerResponse response = new CustomerResponse();

        when(businessCustomerRepository.findByRuc(ruc)).thenReturn(Mono.just(customer));
        when(customerMapper.getCustomerResponse(customer)).thenReturn(response);

        StepVerifier.create(customerService.findByRuc(ruc))
            .expectNext(response)
            .verifyComplete();
    }

    @Test
    void create_WithInvalidCustomerType_ShouldThrowException() {
        CustomerRequest request = new CustomerRequest() {
            @Override
            public String getType() {
                return "INVALID TYPE";
            }
        };

        StepVerifier.create(customerService.create(Mono.just(request)))
            .expectError(InvalidCustomerTypeException.class)
            .verify();
    }

    @Test
    void findByDni_WhenExists_ShouldReturnCustomer() {
        String dni = "12345678";
        PersonalCustomer customer = new PersonalCustomer();
        CustomerResponse response = new CustomerResponse();

        when(personalCustomerRepository.findByDocumentNumber(dni)).thenReturn(Mono.just(customer));
        when(customerMapper.getCustomerResponse(customer)).thenReturn(response);

        StepVerifier.create(customerService.findByDni(dni))
            .expectNext(response)
            .verifyComplete();
    }

    @Test
    void findByDni_WhenNotExists_ShouldThrowException() {
        String dni = "12345678";

        when(personalCustomerRepository.findByDocumentNumber(dni)).thenReturn(Mono.empty());

        StepVerifier.create(customerService.findByDni(dni))
            .expectError(NotFoundException.class)
            .verify();
    }

    @Test
    void update_WhenCustomerExists_ShouldUpdateAndReturnCustomer() {
        String id = "1";
        Customer existingCustomer = new Customer();
        Customer updatedCustomer = new Customer();
        CustomerPatchRequest patchRequest = new CustomerPatchRequest();
        CustomerResponse response = new CustomerResponse();

        when(customerRepository.findById(id)).thenReturn(Mono.just(existingCustomer));
        when(customerMapper.getCustomerUpdateEntity(patchRequest, existingCustomer)).thenReturn(updatedCustomer);
        when(customerRepository.save(updatedCustomer)).thenReturn(Mono.just(updatedCustomer));
        when(customerMapper.getCustomerResponse(updatedCustomer)).thenReturn(response);

        StepVerifier.create(customerService.update(id, Mono.just(patchRequest)))
            .expectNext(response)
            .verifyComplete();
    }

    @Test
    void update_WhenCustomerNotExists_ShouldThrowException() {
        String id = "1";
        CustomerPatchRequest patchRequest = new CustomerPatchRequest();

        when(customerRepository.findById(id)).thenReturn(Mono.empty());

        StepVerifier.create(customerService.update(id, Mono.just(patchRequest)))
            .expectError(NotFoundException.class)
            .verify();
    }

    @Test
    void validateCustomerExists_WhenPersonalCustomerExists_ShouldThrowException() {
        com.project1.ms_customer_service.model.PersonalCustomer personalCustomer = new com.project1.ms_customer_service.model.PersonalCustomer();
        personalCustomer.setType(CustomerType.PERSONAL.toString());
        personalCustomer.setDocumentNumber("12345678");

        when(personalCustomerRepository.findByDocumentNumber("12345678"))
            .thenReturn(Mono.just(new PersonalCustomer()));

        StepVerifier.create(customerService.create(Mono.just(personalCustomer)))
            .expectErrorMatches(throwable -> throwable instanceof BadRequestException)
            .verify();
    }

    @Test
    void validateCustomerExists_WhenBusinessCustomerExists_ShouldThrowException() {
        com.project1.ms_customer_service.model.BusinessCustomer request = new com.project1.ms_customer_service.model.BusinessCustomer();
        request.setType(CustomerType.BUSINESS.toString());
        request.setRuc("12345678");

        when(businessCustomerRepository.findByRuc("12345678"))
            .thenReturn(Mono.just(new BusinessCustomer()));

        StepVerifier.create(customerService.create(Mono.just(request)))
            .expectErrorMatches(throwable -> throwable instanceof BadRequestException)
            .verify();
    }
}
