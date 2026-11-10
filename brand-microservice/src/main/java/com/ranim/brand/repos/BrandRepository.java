package com.ranim.brand.repos;

import com.ranim.brand.entities.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    Brand findByName(String name);
}
