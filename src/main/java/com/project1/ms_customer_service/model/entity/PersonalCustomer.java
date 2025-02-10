package com.project1.ms_customer_service.model.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class PersonalCustomer extends Customer {
    private String documentNumber;
    private String firstName;
    private String lastName;
}
