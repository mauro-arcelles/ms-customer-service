package com.project1.ms_customer_service.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BusinessSubTypeValidator implements ConstraintValidator<ValidBusinessSubType, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || value.equals("PYME");
    }
}
