package com.ranim.product.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String brand;      // name of the brand (same role as depCode in the course)
    private String category;
    private double price;
    // Section 14: contact notified when the product is created (same role as teacher email / mobile)
    private String email;
    private String mobileNumber;
    private boolean communicationDone;
}
