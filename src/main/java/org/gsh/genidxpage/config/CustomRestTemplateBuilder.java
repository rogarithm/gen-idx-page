package org.gsh.genidxpage.config;

import org.springframework.boot.web.client.RestTemplateBuilder;

import java.time.Duration;

public class CustomRestTemplateBuilder {

    public static RestTemplateBuilder get() {
        return new RestTemplateBuilder()
            .readTimeout(Duration.ofSeconds(15L))
            .connectTimeout(Duration.ofSeconds(15L))
            .errorHandler(new CustomRestTemplateErrorHandler());
    }
}
