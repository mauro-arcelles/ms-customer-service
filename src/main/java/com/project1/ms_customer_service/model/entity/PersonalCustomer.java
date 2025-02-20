package com.project1.ms_customer_service.model.entity;

import com.project1.ms_customer_service.validation.ValidPersonalSubType;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@TypeAlias("personalCustomer")
public class PersonalCustomer extends Customer {
    private String documentNumber;

    private String firstName;

    private String lastName;

    @ValidPersonalSubType
    private PersonalCustomerType subType;
}
