package com.project1.ms_customer_service.model.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class BusinessCustomer extends Customer {
    private String ruc;
    private String businessName;
}
