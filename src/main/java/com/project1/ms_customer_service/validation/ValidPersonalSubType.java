package com.project1.ms_customer_service.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PersonalSubTypeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPersonalSubType {
    String message() default "Invalid personal subtype should be one of: VIP";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
