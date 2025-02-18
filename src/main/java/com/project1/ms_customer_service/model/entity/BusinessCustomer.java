package com.project1.ms_customer_service.model.entity;

import com.project1.ms_customer_service.validation.ValidBusinessSubType;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@TypeAlias("businessCustomer")
public class BusinessCustomer extends Customer {
    private String ruc;
    private String businessName;
    @ValidBusinessSubType
    private BusinessCustomerType subType;
}
