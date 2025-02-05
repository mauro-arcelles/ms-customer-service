package com.project1.ms_customer_service.model.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document(collection = "customers")
public class Customer {
    @Id
    private String id;
    private String documentNumber;
    private String firstName;
    private String lastName;
    private CustomerType type;
}
