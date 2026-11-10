package com.ranim.brand;

import com.ranim.brand.entities.Brand;
import com.ranim.brand.repos.BrandRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BrandMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BrandMicroserviceApplication.class, args);
    }

    // 2 brands added at startup
    @Bean
    CommandLineRunner commandLineRunner(BrandRepository brandRepository) {
        return args -> {
            // MySQL keeps its data between restarts: insert the brands only once
            if (brandRepository.count() > 0) {
                return;
            }
            brandRepository.save(Brand.builder()
                    .name("MAC")
                    .country("Canada")
                    .foundedYear(1984)
                    .build());
            brandRepository.save(Brand.builder()
                    .name("Fenty")
                    .country("USA")
                    .foundedYear(2017)
                    .build());
        };
    }
}
