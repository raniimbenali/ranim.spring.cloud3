package com.ranim.product.service;

import com.ranim.product.dto.BrandDto;
import org.springframework.stereotype.Component;

@Component
public class BrandFallBack implements APIClient {

    @Override
    public BrandDto getBrandByName(String brandName) {
        return null;
    }
}
