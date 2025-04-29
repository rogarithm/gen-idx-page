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
            .defaultHeader("User-Agent",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7)",
                "AppleWebKit/537.36 (KHTML, like Gecko)",
                "Chrome/134.0.0.0",
                "Safari/537.36")
            .readTimeout(Duration.ofSeconds(15L))
            .connectTimeout(Duration.ofSeconds(15L))
            .errorHandler(new CustomRestTemplateErrorHandler());
    }
}
