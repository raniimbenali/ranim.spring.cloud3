package com.ranim.message.functions;

import com.ranim.message.dto.ProductMessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class MessageFunctions {

    private static final Logger log = LoggerFactory.getLogger(MessageFunctions.class);

    @Bean
    public Function<ProductMessageDto, ProductMessageDto> email() {
        return productMsgDto -> {
            log.info("Sending email with the details : " + productMsgDto.toString());
            return productMsgDto;
        };
    }

    @Bean
    public Function<ProductMessageDto, Long> sms() {
        return productMsgDto -> {
            log.info("Sending sms with the details : " + productMsgDto.toString());
            return productMsgDto.productId();
        };
    }
}
