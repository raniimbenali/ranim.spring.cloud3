package com.ranim.product.service;

import com.ranim.product.dto.APIResponseDto;
import com.ranim.product.dto.BrandDto;
import com.ranim.product.dto.ProductDto;
import com.ranim.product.dto.ProductMessageDto;
import com.ranim.product.entities.Product;
import com.ranim.product.exceptions.ResourceNotFoundException;
import com.ranim.product.repos.ProductRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    private ProductRepository productRepository;
    private APIClient apiClient;
    private final StreamBridge streamBridge;

    @Override
    public APIResponseDto getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Product", "id", id.toString()));

        BrandDto brandDto = apiClient.getBrandByName(product.getBrand());
        String country = brandDto == null ? "NOT AVAILABLE" : brandDto.getCountry();

        ProductDto productDto = new ProductDto(
                product.getId(),
                product.getName(),
                product.getBrand(),
                product.getCategory(),
                product.getPrice(),
                country,
                product.getEmail(),
                product.getMobileNumber(),
                product.isCommunicationDone()
        );

        APIResponseDto apiResponseDto = new APIResponseDto();
        apiResponseDto.setProductDto(productDto);
        apiResponseDto.setBrandDto(brandDto);
        return apiResponseDto;
    }

    @Override
    public void createProduct(ProductDto productDto) {
        Product savedProduct = productRepository.save(Product.builder()
                .name(productDto.getName())
                .brand(productDto.getBrand())
                .category(productDto.getCategory())
                .price(productDto.getPrice())
                .email(productDto.getEmail())
                .mobileNumber(productDto.getMobileNumber())
                .communicationDone(false)
                .build());
        sendCommunication(savedProduct);
    }

    @Override
    public boolean updateCommunicationStatus(Long productId) {
        if (productId == null) {
            return false;
        }
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product", "productId", productId.toString()));
        product.setCommunicationDone(true);
        productRepository.save(product);
        return true;
    }

    private void sendCommunication(Product product) {
        var productMsgDto = new ProductMessageDto(product.getId(), product.getEmail(), product.getMobileNumber());
        log.info("Sending Communication request for the details: {}", productMsgDto);
        var result = streamBridge.send("sendCommunication-out-0",
                MessageBuilder.withPayload(productMsgDto)
                        .setHeader("contentType", "application/json")
                        .build());
        log.info("Is the Communication request successfully triggered ? : {}", result);
    }
}
