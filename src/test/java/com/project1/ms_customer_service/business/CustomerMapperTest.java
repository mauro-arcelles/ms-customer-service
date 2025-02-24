package com.project1.ms_customer_service.business;

import com.project1.ms_customer_service.exception.BadRequestException;
import com.project1.ms_customer_service.model.CustomerPatchRequest;
import com.project1.ms_customer_service.model.CustomerResponse;
import com.project1.ms_customer_service.model.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

@SpringBootTest
class CustomerMapperTest {

    @Autowired
    private CustomerMapper customerMapper;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(customerMapper, "personalVipAvgDailyMinimumAmount", new BigDecimal("1000.00"));
    }

    @Test
    void getCustomerResponse_Personal() {
        PersonalCustomer customer = new PersonalCustomer();
        customer.setId("123");
        customer.setType(CustomerType.PERSONAL);
        customer.setStatus(CustomerStatus.ACTIVE);
        customer.setDocumentNumber("12345");
        customer.setFirstName("John");
        customer.setLastName("Doe");

        CustomerResponse response = customerMapper.getCustomerResponse(customer);

        assertEquals("123", response.getId());
        assertEquals("PERSONAL", response.getType());
        assertEquals("ACTIVE", response.getStatus());
        assertEquals("12345", response.getDocumentNumber());
        assertEquals("John", response.getFirstName());
        assertEquals("Doe", response.getLastName());
    }

    @Test
    void getCustomerResponse_PersonalVip() {
        PersonalVipCustomer customer = PersonalVipCustomer.builder()
            .id("123")
            .type(CustomerType.PERSONAL)
            .status(CustomerStatus.ACTIVE)
            .documentNumber("12345")
            .firstName("John")
            .lastName("Doe")
            .subType(PersonalCustomerType.VIP)
            .avgDailyMinimumAmount(new BigDecimal("1000"))
            .build();

        CustomerResponse response = customerMapper.getCustomerResponse(customer);

        assertEquals(PersonalCustomerType.VIP.toString(), response.getSubType());
        assertEquals(new BigDecimal("1000"), response.getAvgDailyMinimumAmount());
    }

    @Test
    void getCustomerResponse_Business() {
        BusinessCustomer customer = BusinessCustomer.builder()
            .id("123")
            .type(CustomerType.BUSINESS)
            .status(CustomerStatus.ACTIVE)
            .ruc("20123456789")
            .subType(BusinessCustomerType.PYME)
            .build();

        CustomerResponse response = customerMapper.getCustomerResponse(customer);

        assertEquals("20123456789", response.getRuc());
        assertEquals(BusinessCustomerType.PYME.toString(), response.getSubType());
    }

    @Test
    void getCustomerEntity_PersonalVip() {
        com.project1.ms_customer_service.model.PersonalCustomer request =
            new com.project1.ms_customer_service.model.PersonalCustomer();
        request.setType("PERSONAL");
        request.setSubType("VIP");
        request.setDocumentNumber("12345");
        request.setFirstName("John");
        request.setLastName("Doe");

        Customer customer = customerMapper.getCustomerEntity(request);

        assertInstanceOf(PersonalVipCustomer.class, customer);
        assertEquals(PersonalCustomerType.VIP, ((PersonalVipCustomer) customer).getSubType());
        assertEquals(new BigDecimal("1000.00"), ((PersonalVipCustomer) customer).getAvgDailyMinimumAmount());
    }

    @Test
    void getCustomerUpdateEntity_Personal() {
        CustomerPatchRequest request = new CustomerPatchRequest();
        request.setFirstName("Jane");

        PersonalCustomer existingCustomer = PersonalCustomer.builder()
            .type(CustomerType.PERSONAL)
            .firstName("John")
            .build();

        Customer updated = customerMapper.getCustomerUpdateEntity(request, existingCustomer);

        assertEquals("Jane", ((PersonalCustomer) updated).getFirstName());
    }

    @Test
    void getCustomerUpdateEntity_Business() {
        CustomerPatchRequest request = new CustomerPatchRequest();
        request.setBusinessName("New Corp");

        BusinessCustomer existingCustomer = BusinessCustomer.builder()
            .type(CustomerType.BUSINESS)
            .businessName("Old Corp")
            .build();

        Customer updated = customerMapper.getCustomerUpdateEntity(request, existingCustomer);

        assertEquals("New Corp", ((BusinessCustomer) updated).getBusinessName());
    }

    @Test
    void getCustomerUpdateEntity_ThrowsException() {
        CustomerPatchRequest request = new CustomerPatchRequest();
        PersonalCustomer existingCustomer = new PersonalCustomer();
        existingCustomer.setType(CustomerType.PERSONAL);

        assertThrows(BadRequestException.class, () ->
            customerMapper.getCustomerUpdateEntity(request, existingCustomer));
    }

    @Test
    void getCustomerUpdateEntity_Business_ThrowsBadRequest() {
        CustomerPatchRequest request = new CustomerPatchRequest();
        BusinessCustomer existingCustomer = new BusinessCustomer();
        existingCustomer.setType(CustomerType.BUSINESS);

        BadRequestException exception = assertThrows(BadRequestException.class, () ->
            customerMapper.getCustomerUpdateEntity(request, existingCustomer));

        assertEquals("At least businessName should be provided", exception.getMessage());
    }

    @Test
    void getCustomerEntity_PersonalRegular() {
        com.project1.ms_customer_service.model.PersonalCustomer request =
            new com.project1.ms_customer_service.model.PersonalCustomer();
        request.setType("PERSONAL");
        request.setDocumentNumber("12345");
        request.setFirstName("John");
        request.setLastName("Doe");

        Customer customer = customerMapper.getCustomerEntity(request);

        assertInstanceOf(PersonalCustomer.class, customer);
        assertEquals(CustomerType.PERSONAL, customer.getType());
        assertEquals("12345", ((PersonalCustomer) customer).getDocumentNumber());
        assertEquals("John", ((PersonalCustomer) customer).getFirstName());
        assertEquals("Doe", ((PersonalCustomer) customer).getLastName());
        assertEquals(CustomerStatus.ACTIVE, customer.getStatus());
        assertNull(((PersonalCustomer) customer).getSubType());
    }

    @Test
    void getCustomerEntity_Business() {
        com.project1.ms_customer_service.model.BusinessCustomer request =
            new com.project1.ms_customer_service.model.BusinessCustomer();
        request.setType("BUSINESS");
        request.setRuc("20123456789");
        request.setBusinessName("Test Corp");
        request.setSubType(BusinessCustomerType.PYME.toString());

        Customer customer = customerMapper.getCustomerEntity(request);

        assertInstanceOf(BusinessCustomer.class, customer);
        assertEquals(CustomerType.BUSINESS, customer.getType());
        assertEquals("20123456789", ((BusinessCustomer) customer).getRuc());
        assertEquals("Test Corp", ((BusinessCustomer) customer).getBusinessName());
        assertEquals(CustomerStatus.ACTIVE, customer.getStatus());
        assertEquals(BusinessCustomerType.PYME, ((BusinessCustomer) customer).getSubType());
    }
}
