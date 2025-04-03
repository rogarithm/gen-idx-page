package org.gsh.genidxpage.service;

import org.gsh.genidxpage.config.CustomRestTemplateBuilder;
import org.gsh.genidxpage.service.dto.FindBlogPostDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WebArchiveApiCaller {

    private final RestTemplate restTemplate;

    public WebArchiveApiCaller(@Value("${webArchive.rootUri}") final String rootUri) {
        RestTemplate restTemplate = CustomRestTemplateBuilder.get().rootUri(rootUri).build();
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<String> findBlogPost(final String restUrl, final FindBlogPostDto dto) {
        return restTemplate.getForEntity(restUrl, String.class, dto.getYear(), dto.getMonth());
    }
}
