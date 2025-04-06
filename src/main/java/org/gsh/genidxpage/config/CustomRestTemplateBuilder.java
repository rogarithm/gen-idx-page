package org.gsh.genidxpage.config;

import java.time.Duration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
