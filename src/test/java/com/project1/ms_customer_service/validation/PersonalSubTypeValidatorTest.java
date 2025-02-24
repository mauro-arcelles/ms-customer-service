package com.project1.ms_customer_service.validation;

import com.project1.ms_customer_service.model.entity.PersonalCustomerType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PersonalSubTypeValidatorTest {

    @Test
    void testPersonalSubTypeValidator() {
        PersonalSubTypeValidator validator = new PersonalSubTypeValidator();

        assertTrue(validator.isValid(PersonalCustomerType.VIP.toString(), null));
        assertTrue(validator.isValid(null, null));
        assertFalse(validator.isValid("OTHER", null));
    }
}
