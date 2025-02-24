package com.project1.ms_customer_service;

import com.project1.ms_customer_service.business.CustomerService;
import com.project1.ms_customer_service.model.CustomerPatchRequest;
import com.project1.ms_customer_service.model.CustomerResponse;
import com.project1.ms_customer_service.model.PersonalCustomer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@SpringBootTest
class CustomerApiDelegateImplTest {

    @MockBean
    private CustomerService customerService;

    @Autowired
    private CustomerApiDelegateImpl customerApiDelegate;

    @Test
    void createCustomer() {
        PersonalCustomer request = new PersonalCustomer();
        CustomerResponse response = new CustomerResponse();

        when(customerService.create(any(Mono.class)))
            .thenReturn(Mono.just(response));

        StepVerifier.create(customerApiDelegate.createCustomer(Mono.just(request), null))
            .expectNextMatches(responseEntity ->
                responseEntity.getStatusCode() == HttpStatus.CREATED &&
                    responseEntity.getBody() == response)
            .verifyComplete();
    }

    @Test
    void deleteCustomer() {
        when(customerService.delete("1")).thenReturn(Mono.empty());

        StepVerifier.create(customerApiDelegate.deleteCustomer("1", null))
            .expectComplete()
            .verify();
    }

    @Test
    void getCustomer() {
        CustomerResponse response = new CustomerResponse();
        when(customerService.findById("1")).thenReturn(Mono.just(response));

        StepVerifier.create(customerApiDelegate.getCustomer("1", null))
            .expectNextMatches(responseEntity ->
                responseEntity.getStatusCode() == HttpStatus.OK &&
                    responseEntity.getBody() == response)
            .verifyComplete();
    }

    @Test
    void getCustomers() {
        CustomerResponse response = new CustomerResponse();
        when(customerService.findAll()).thenReturn(Flux.just(response));

        StepVerifier.create(customerApiDelegate.getCustomers(null))
            .expectNextMatches(responseEntity ->
                responseEntity.getStatusCode() == HttpStatus.OK)
            .verifyComplete();
    }

    @Test
    void updateCustomer() {
        CustomerPatchRequest request = new CustomerPatchRequest();
        CustomerResponse response = new CustomerResponse();

        when(customerService.update(eq("1"), any())).thenReturn(Mono.just(response));

        StepVerifier.create(customerApiDelegate.updateCustomer("1", Mono.just(request), null))
            .expectNextMatches(responseEntity ->
                responseEntity.getStatusCode() == HttpStatus.OK &&
                    responseEntity.getBody() == response)
            .verifyComplete();
    }

    @Test
    void getCustomerByDni() {
        CustomerResponse response = new CustomerResponse();
        when(customerService.findByDni("12345678")).thenReturn(Mono.just(response));

        StepVerifier.create(customerApiDelegate.getCustomerByDni("12345678", null))
            .expectNextMatches(responseEntity ->
                responseEntity.getStatusCode() == HttpStatus.OK &&
                    responseEntity.getBody() == response)
            .verifyComplete();
    }

    @Test
    void getCustomerByRuc() {
        CustomerResponse response = new CustomerResponse();
        when(customerService.findByRuc("12345678901")).thenReturn(Mono.just(response));

        StepVerifier.create(customerApiDelegate.getCustomerByRuc("12345678901", null))
            .expectNextMatches(responseEntity ->
                responseEntity.getStatusCode() == HttpStatus.OK &&
                    responseEntity.getBody() == response)
            .verifyComplete();
    }
}
