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

@Component
public class WebArchiveApiCaller {

    private final RestTemplate restTemplate;
    private final String checkArchivedUri;
    private String rootUri;

    public WebArchiveApiCaller(
        @Value("${webArchive.rootUri}") final String rootUri,
        @Value("${webArchive.checkArchivedUri}") String checkArchivedUri,
        RestTemplateBuilder restTemplateBuilder
    ) {
        this.restTemplate = restTemplateBuilder.rootUri(rootUri).build();
        this.checkArchivedUri = checkArchivedUri;
        this.rootUri = rootUri;
    }

    public ArchivedPageInfo findArchivedPageInfo(final CheckPostArchivedDto dto) {
        String checkArchivedUrl = "/wayback/available";
        UriComponents uri = UriComponentsBuilder.fromUriString(rootUri + checkArchivedUrl)
            .queryParam("url", dto.getUrl())
            .queryParam("timestamp", dto.getTimestamp())
            .build();
        ResponseEntity<String> archivedPageInfo = restTemplate.getForEntity(
            uri.toUriString(),
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

    public boolean isArchived(final ArchivedPageInfo archivedPageInfo) {
        return archivedPageInfo.isAccessible();
    }

    public ResponseEntity<String> findBlogPostPage(final ArchivedPageInfo archivedPageInfo) {
        return restTemplate.getForEntity(archivedPageInfo.accessibleUrl(), String.class);
    }
}
