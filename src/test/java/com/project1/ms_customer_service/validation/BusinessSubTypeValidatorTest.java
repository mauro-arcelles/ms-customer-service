package com.project1.ms_customer_service.validation;

import com.project1.ms_customer_service.model.entity.BusinessCustomerType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BusinessSubTypeValidatorTest {

    @Test
    void testBusinessSubTypeValidator() {
        BusinessSubTypeValidator validator = new BusinessSubTypeValidator();

        assertTrue(validator.isValid(BusinessCustomerType.PYME.toString(), null));
        assertTrue(validator.isValid(null, null));
        assertFalse(validator.isValid("OTHER", null));
    }
}
