package com.project1.ms_customer_service.business;

import com.project1.ms_customer_service.exception.BadRequestException;
import com.project1.ms_customer_service.exception.InvalidCustomerTypeException;
import com.project1.ms_customer_service.model.CustomerPatchRequest;
import com.project1.ms_customer_service.model.CustomerRequest;
import com.project1.ms_customer_service.model.CustomerResponse;
import com.project1.ms_customer_service.model.entity.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class CustomerMapper {

    @Value("${customer.config.personal.vip.avgDailyMinimumAmount}")
    private BigDecimal personalVipAvgDailyMinimumAmount;

    public CustomerResponse getCustomerResponse(Customer customer) {
        CustomerResponse response = new CustomerResponse();
        response.setId(customer.getId());
        response.setType(customer.getType().name());
        response.setStatus(customer.getStatus().toString());
        if (customer instanceof PersonalCustomer) {
            PersonalCustomer personalCustomer = (PersonalCustomer) customer;
            response.setDocumentNumber(personalCustomer.getDocumentNumber());
            response.setFirstName(personalCustomer.getFirstName());
            response.setLastName(personalCustomer.getLastName());
        }
        if (customer instanceof PersonalVipCustomer) {
            PersonalVipCustomer personalVipCustomer = (PersonalVipCustomer) customer;
            if (personalVipCustomer.getSubType() != null) {
                response.setSubType(personalVipCustomer.getSubType().toString());
                response.setAvgDailyMinimumAmount(personalVipCustomer.getAvgDailyMinimumAmount());
            }
        }
        if (customer instanceof BusinessCustomer) {
            BusinessCustomer businessCustomer = (BusinessCustomer) customer;
            response.setRuc(businessCustomer.getRuc());
            response.setSubType(Optional.ofNullable(businessCustomer.getSubType())
                .map(Object::toString)
                .orElse(null));
        }
        return response;
    }

    public Customer getCustomerEntity(CustomerRequest request) {
        CustomerType type = CustomerType.valueOf(request.getType());

        switch (type) {
            case PERSONAL:
                com.project1.ms_customer_service.model.PersonalCustomer personalCustomer = (com.project1.ms_customer_service.model.PersonalCustomer) request;

                if (personalCustomer.getSubType() != null) {
                    return PersonalVipCustomer.builder()
                        .type(CustomerType.PERSONAL)
                        .documentNumber(personalCustomer.getDocumentNumber())
                        .firstName(personalCustomer.getFirstName())
                        .lastName(personalCustomer.getLastName())
                        .status(CustomerStatus.ACTIVE)
                        .subType(PersonalCustomerType.valueOf(personalCustomer.getSubType()))
                        .avgDailyMinimumAmount(personalVipAvgDailyMinimumAmount)
                        .build();
                }

                return PersonalCustomer.builder()
                    .type(CustomerType.PERSONAL)
                    .documentNumber(personalCustomer.getDocumentNumber())
                    .firstName(personalCustomer.getFirstName())
                    .lastName(personalCustomer.getLastName())
                    .status(CustomerStatus.ACTIVE)
                    .subType(null)
                    .build();

            case BUSINESS:
                com.project1.ms_customer_service.model.BusinessCustomer businessCustomer = (com.project1.ms_customer_service.model.BusinessCustomer) request;
                return BusinessCustomer.builder()
                    .type(CustomerType.BUSINESS)
                    .ruc(businessCustomer.getRuc())
                    .businessName(businessCustomer.getBusinessName())
                    .status(CustomerStatus.ACTIVE)
                    .subType(businessCustomer.getSubType() != null ? BusinessCustomerType.valueOf(businessCustomer.getSubType()) : null)
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
