package org.gsh.genidxpage.service;

import org.gsh.genidxpage.service.dto.ArchivedPageInfo;
import org.gsh.genidxpage.service.dto.CheckPostArchivedDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

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

    public ArchivedPageInfo findArchivedPageInfo(final CheckPostArchivedDto dto) {
        String uri = buildUri(dto);

        ResponseEntity<String> archivedPageInfo = restTemplate.getForEntity(
            uri,
            String.class
        );

        String response = archivedPageInfo.getBody();
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(response, ArchivedPageInfo.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    String buildUri(final CheckPostArchivedDto dto) {
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(checkArchivedUri).build();

        List<String> queryParams = new ArrayList<>();
        queryParams.add(dto.getUrl());
        if (uriComponents.getQueryParams().get("timestamp") != null) {
            queryParams.add(dto.getTimestamp());
        }

        return UriComponentsBuilder.fromUriString(rootUri)
            .uriComponents(
                uriComponents.expand(queryParams.toArray())
            )
            .build().toUriString();
    }

    public boolean isArchived(final ArchivedPageInfo archivedPageInfo) {
        return archivedPageInfo.isAccessible();
    }

    public ResponseEntity<String> findBlogPostPage(final ArchivedPageInfo archivedPageInfo) {
        return restTemplate.getForEntity(archivedPageInfo.accessibleUrl(), String.class);
    }
}
