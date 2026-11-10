package com.ranim.product.service;

import com.ranim.product.dto.BrandDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient(url = "http://localhost:8080", value = "BRAND")
@FeignClient(name = "BRAND", fallback = BrandFallBack.class)
public interface APIClient {

    @GetMapping("api/brands/{brand-name}")
    BrandDto getBrandByName(@PathVariable("brand-name") String brandName);
}
