package org.gsh.genidxpage.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.gsh.genidxpage.service.dto.ArchivedPageInfo;
import org.gsh.genidxpage.service.dto.CheckPostArchivedDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WebArchiveApiCaller {

    private final RestTemplate restTemplate;
    private final String checkArchivedUri;

    public WebArchiveApiCaller(
        @Value("${webArchive.rootUri}") final String rootUri,
        @Value("${webArchive.checkArchivedUri}") String checkArchivedUri,
        RestTemplateBuilder restTemplateBuilder
    ) {
        this.restTemplate = restTemplateBuilder.rootUri(rootUri).build();
        this.checkArchivedUri = checkArchivedUri;
    }

    public ArchivedPageInfo findArchivedPageInfo(final CheckPostArchivedDto dto) {
        ResponseEntity<String> archivedPageInfo = restTemplate.getForEntity(
            checkArchivedUri,
            String.class,
            dto.getUrl(),
            dto.getTimestamp()
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
