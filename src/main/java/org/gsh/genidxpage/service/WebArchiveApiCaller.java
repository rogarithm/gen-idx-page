package org.gsh.genidxpage.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.gsh.genidxpage.service.dto.ArchivedPageInfo;
import org.gsh.genidxpage.service.dto.CheckPostArchivedDto;
import org.gsh.genidxpage.service.dto.FindBlogPostDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WebArchiveApiCaller {

    private final RestTemplate restTemplate;

    public WebArchiveApiCaller(@Value("${webArchive.rootUri}") final String rootUri, RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.rootUri(rootUri).build();
    }

    public ResponseEntity<String> findBlogPost(final String restUrl, final FindBlogPostDto dto) {
        return restTemplate.getForEntity(restUrl, String.class, dto.getYear(), dto.getMonth());
    }

    public ArchivedPageInfo findArchivedPageInfo(
        @Value("${webArchive.checkArchivedUri}") final String checkArchivedUri,
        final CheckPostArchivedDto dto
    ) {
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

    public boolean isArchived(
        @Value("${webArchive.checkArchivedUri}") final String checkArchivedUri,
        final CheckPostArchivedDto dto
    ) {
        ResponseEntity<String> archivedPageInfo = restTemplate.getForEntity(
            checkArchivedUri,
            String.class,
            dto.getUrl(),
            dto.getTimestamp()
        );

        String response = archivedPageInfo.getBody();
        ObjectMapper mapper = new ObjectMapper();
        ArchivedPageInfo pageInfo = null;
        try {
            pageInfo = mapper.readValue(response, ArchivedPageInfo.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return pageInfo.getArchivedSnapshots() != null;
    }
}
