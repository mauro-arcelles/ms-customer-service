package com.project1.ms_customer_service.business;

import com.project1.ms_customer_service.model.CustomerRequest;
import com.project1.ms_customer_service.model.CustomerResponse;
import com.project1.ms_customer_service.model.entity.Customer;
import com.project1.ms_customer_service.model.entity.CustomerType;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {
    public CustomerResponse getCustomerResponse(Customer customer) {
        CustomerResponse response = new CustomerResponse();
        response.setId(customer.getId());
        response.setDocumentNumber(customer.getDocumentNumber());
        response.setFirstName(customer.getFirstName());
        response.setLastName(customer.getLastName());
        response.setType(customer.getType().name());
        return response;
    }

    public Customer getCustomerEntity(CustomerRequest request, String id) {
        Customer customer = new Customer();
        customer.setId(id);
        customer.setType(CustomerType.valueOf(request.getType()));
        customer.setDocumentNumber(request.getDocumentNumber());
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        return customer;
    }
}
