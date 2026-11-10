package com.ranim.product.service;

import com.ranim.product.dto.APIResponseDto;
import com.ranim.product.dto.ProductDto;

public interface ProductService {
    APIResponseDto getProductById(Long id);

    void createProduct(ProductDto productDto);

    boolean updateCommunicationStatus(Long productId);
}
