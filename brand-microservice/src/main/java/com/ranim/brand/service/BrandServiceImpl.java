package com.ranim.brand.service;

import com.ranim.brand.dto.BrandDto;
import com.ranim.brand.entities.Brand;
import com.ranim.brand.repos.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    BrandRepository brandRepository;

    @Override
    public BrandDto getBrandByName(String name) {
        Brand brand = brandRepository.findByName(name);
        BrandDto brandDto = new BrandDto(
                brand.getId(),
                brand.getName(),
                brand.getCountry(),
                brand.getFoundedYear()
        );
        return brandDto;
    }
}
