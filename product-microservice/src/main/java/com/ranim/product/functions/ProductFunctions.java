package com.ranim.product.functions;

import com.ranim.product.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class ProductFunctions {

    private static final Logger log = LoggerFactory.getLogger(ProductFunctions.class);

    @Bean
    public Consumer<Long> updateCommunication(ProductService productService) {
        return productId -> {
            log.info("Updating Communication status for the product Id : {}", productId);
            productService.updateCommunicationStatus(productId);
        };
    }
}
