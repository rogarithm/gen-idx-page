package org.gsh.genidxpage.service;

import org.gsh.genidxpage.service.dto.ArchivedPageInfo;
import org.gsh.genidxpage.service.dto.UnreachableArchivedPageInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class WebArchiveApiCaller {

    private final RestTemplate restTemplate;
    private final String checkArchivedUri;
    private final String rootUri;

    public WebArchiveApiCaller(
        @Value("${web-archive.root-uri}") final String rootUri,
        @Value("${web-archive.check-archived-uri}") String checkArchivedUri,
        RestTemplateBuilder restTemplateBuilder
    ) {
        this.restTemplate = restTemplateBuilder.build();
        this.checkArchivedUri = checkArchivedUri;
        this.rootUri = rootUri;
    }

    public ArchivedPageInfo findArchivedPageInfo(String url, String timestamp) {
        String uri = buildUri(url, timestamp);
        ResponseEntity<String> archivedPageInfo;

        try {
            archivedPageInfo = restTemplate.getForEntity(
                uri,
                String.class
            );
        } catch (ResourceAccessException e) {
            log.warn("timeout error: {}", uri, e);
            return new UnreachableArchivedPageInfo();
        }

        String response = archivedPageInfo.getBody();
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(response, ArchivedPageInfo.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    String buildUri(String url, String timestamp) {
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(checkArchivedUri).build();

        List<String> queryParams = new ArrayList<>();
        queryParams.add(url);
        if (uriComponents.getQueryParams().get("timestamp") != null) {
            queryParams.add(timestamp);
        }

        return UriComponentsBuilder.fromUriString(rootUri)
            .uriComponents(
                uriComponents.expand(queryParams.toArray())
            )
            .build().toUriString();
    }

    boolean isArchived(final ArchivedPageInfo archivedPageInfo) {
        return archivedPageInfo.isAccessible();
    }

    ResponseEntity<String> findBlogPostPage(final ArchivedPageInfo archivedPageInfo) {
        return restTemplate.getForEntity(archivedPageInfo.accessibleUrl(), String.class);
    }
}
