package com.ranim.product;

import com.ranim.product.entities.Product;
import com.ranim.product.repos.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@EnableFeignClients
public class ProductMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductMicroserviceApplication.class, args);
    }

    // PDF 02: WebClient configured as a Spring Bean
    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }

    // 2 products added at startup
    @Bean
    CommandLineRunner commandLineRunner(ProductRepository productRepository) {
        return args -> {
            productRepository.save(Product.builder()
                    .name("Retro Matte Lipstick")
                    .brand("MAC")
                    .category("lipstick")
                    .price(23.0)
                    .build());
            productRepository.save(Product.builder()
                    .name("Pro Filtr Soft Matte Foundation")
                    .brand("Fenty")
                    .category("foundation")
                    .price(40.0)
                    .build());
        };
    }
}
