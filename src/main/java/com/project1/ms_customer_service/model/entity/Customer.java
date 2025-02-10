package com.project1.ms_customer_service.model.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document(collection = "customers")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class Customer {
    @Id
    private String id;
    private CustomerType type;
}
