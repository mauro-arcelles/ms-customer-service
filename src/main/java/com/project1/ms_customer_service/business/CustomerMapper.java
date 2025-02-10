package com.project1.ms_customer_service.business;

import com.project1.ms_customer_service.exception.BadRequestException;
import com.project1.ms_customer_service.exception.InvalidCustomerTypeException;
import com.project1.ms_customer_service.model.CustomerPatchRequest;
import com.project1.ms_customer_service.model.CustomerRequest;
import com.project1.ms_customer_service.model.CustomerResponse;
import com.project1.ms_customer_service.model.entity.BusinessCustomer;
import com.project1.ms_customer_service.model.entity.Customer;
import com.project1.ms_customer_service.model.entity.CustomerType;
import com.project1.ms_customer_service.model.entity.PersonalCustomer;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomerMapper {
    public CustomerResponse getCustomerResponse(Customer customer) {
        CustomerResponse response = new CustomerResponse();
        response.setId(customer.getId());
        response.setType(customer.getType().name());
        if (customer.getType().equals(CustomerType.PERSONAL)) {
            response.setDocumentNumber(((PersonalCustomer) customer).getDocumentNumber());
            response.setFirstName(((PersonalCustomer) customer).getFirstName());
            response.setLastName(((PersonalCustomer) customer).getLastName());
        }
        if (customer.getType().equals(CustomerType.BUSINESS)) {
            response.setRuc(((BusinessCustomer) customer).getRuc());
        }
        return response;
    }

    public Customer getCustomerEntity(CustomerRequest request, String id) {
        CustomerType type = CustomerType.valueOf(request.getType());

        switch(type) {
            case PERSONAL:
                com.project1.ms_customer_service.model.PersonalCustomer personalCustomer = (com.project1.ms_customer_service.model.PersonalCustomer) request;
                return PersonalCustomer.builder()
                        .id(id)
                        .type(CustomerType.PERSONAL)
                        .documentNumber(personalCustomer.getDocumentNumber())
                        .firstName(personalCustomer.getFirstName())
                        .lastName(personalCustomer.getLastName())
                        .build();

            case BUSINESS:
                com.project1.ms_customer_service.model.BusinessCustomer businessCustomer = (com.project1.ms_customer_service.model.BusinessCustomer) request;
                return BusinessCustomer.builder()
                        .id(id)
                        .type(CustomerType.BUSINESS)
                        .ruc(businessCustomer.getRuc())
                        .businessName(businessCustomer.getBusinessName())
                        .build();

            default:
                throw new InvalidCustomerTypeException();
        }
    }

    public Customer getCustomerUpdateEntity(CustomerPatchRequest request, Customer existingCustomer) {
        switch (existingCustomer.getType()) {
            case PERSONAL:
                if (request.getFirstName() == null && request.getLastName() == null) {
                    throw new BadRequestException("At least firstName|lastName should be provided");
                }
                Optional.ofNullable(request.getFirstName()).ifPresent(c -> ((PersonalCustomer) existingCustomer).setFirstName(c));
                Optional.ofNullable(request.getLastName()).ifPresent(c -> ((PersonalCustomer) existingCustomer).setLastName(c));
                break;
            case BUSINESS:
                if (request.getBusinessName() == null) {
                    throw new BadRequestException("At least businessName should be provided");
                }
                Optional.ofNullable(request.getBusinessName()).ifPresent(c -> ((BusinessCustomer) existingCustomer).setBusinessName(c));
                break;
            default:
                throw new IllegalArgumentException("Invalid customer type");
        }

        return existingCustomer;
    }
}
