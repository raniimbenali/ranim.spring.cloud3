package com.ranim.product.restControllers;

import com.ranim.product.config.Configuration;
import com.ranim.product.dto.APIResponseDto;
import com.ranim.product.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.ranim.product.dto.ProductDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
public class ProductController {

    private ProductService productService;

    @Autowired
    Configuration configuration;

    @GetMapping("{id}")
    public ResponseEntity<APIResponseDto> getProductById(@PathVariable("id") Long id) {
        return new ResponseEntity<APIResponseDto>(
                productService.getProductById(id), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createProduct(@RequestBody ProductDto productDto) {
        productService.createProduct(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Product created successfully");
    }

    // PDF 09: /author api
    @GetMapping("/author")
    public ResponseEntity<String> retrieveAuthorInfo() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(configuration.getName() + " " + configuration.getEmail());
    }
}
