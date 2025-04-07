package org.gsh.genidxpage.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class CustomRestTemplateBuilder {

    @Bean
    public static RestTemplateBuilder get() {
        return new RestTemplateBuilder()
            .readTimeout(Duration.ofSeconds(15L))
            .connectTimeout(Duration.ofSeconds(15L))
            .errorHandler(new CustomRestTemplateErrorHandler());
    }
}
