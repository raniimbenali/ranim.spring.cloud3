package com.ranim.brand.restControllers;

import com.ranim.brand.config.Configuration;
import com.ranim.brand.dto.BrandDto;
import com.ranim.brand.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/brands")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @Autowired
    Configuration configuration;

    // PDF 05: @Value (default 1.0 so the service still starts if the Config Server is not reachable)
    @Value("${build.version:1.0}")
    private String buildVersion;

    @GetMapping("{name}")
    public ResponseEntity<BrandDto> getBrandByName(@PathVariable("name") String name) {
        return new ResponseEntity<BrandDto>(
                brandService.getBrandByName(name), HttpStatus.OK);
    }

    @GetMapping("/version")
    public ResponseEntity<String> version() {
        return ResponseEntity.status(HttpStatus.OK).body(buildVersion);
    }

    // PDF 05: @ConfigurationProperties
    @GetMapping("/author")
    public ResponseEntity<String> retrieveAuthorInfo() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(configuration.getName() + " " + configuration.getEmail());
    }
}
