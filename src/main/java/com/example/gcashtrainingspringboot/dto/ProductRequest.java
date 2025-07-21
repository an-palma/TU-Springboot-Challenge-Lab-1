package com.example.gcashtrainingspringboot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

//POJO for JSON structure from POST/PUT reqs
@Data
public class ProductRequest {
    @NotBlank(message = "Product name cannot be empty.")
    private String name;

    @NotNull(message = "Price cannot be empty.")
    @Positive(message = "Price must be a positive number only.")
    private Double price;
}
