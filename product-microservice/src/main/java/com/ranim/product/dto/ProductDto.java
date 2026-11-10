package com.ranim.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private String brand;
    private String category;
    private double price;
    private String brandCountry;   // taken from the Brand microservice (PDF 02: name of the department added to TeacherDto)
    private String email;
    private String mobileNumber;
    private boolean communicationDone;
}
