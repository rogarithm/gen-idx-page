package org.gsh.genidxpage.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.gsh.genidxpage.FakeWebArchiveServer;
import org.gsh.genidxpage.config.CustomRestTemplateBuilder;
import org.gsh.genidxpage.service.dto.ArchivedPageInfo;
import org.gsh.genidxpage.service.dto.ArchivedPageInfoBuilder;
import org.gsh.genidxpage.service.dto.CheckPostArchivedDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.util.UriComponentsBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;

class WebArchiveApiCallerTest {

    @DisplayName("찾으려는 페이지가 web archive 서버에 어떻게 아카이빙되어 있는지 확인할 수 있다")
    @Test
    public void find_how_a_page_is_archived_in_web_archive_server() {
        WebArchiveApiCaller caller = new WebArchiveApiCaller("http://localhost:8080",
            "/wayback/available?url={url}&timestamp={timestamp}",
            CustomRestTemplateBuilder.get());

        FakeWebArchiveServer fakeWebArchiveServer = new FakeWebArchiveServer();
        fakeWebArchiveServer.respondItHasArchivedPage();
        fakeWebArchiveServer.start();

        CheckPostArchivedDto dto = new CheckPostArchivedDto("2021", "3");
        ArchivedPageInfo archivedPageInfo = caller.findArchivedPageInfo(dto);

        // 페이지가 아카이빙되어 있는 경우
        assertThat(caller.isArchived(archivedPageInfo)).isTrue();

        fakeWebArchiveServer.respondItHasNoArchivedPage();
        CheckPostArchivedDto dto2 = new CheckPostArchivedDto("1999", "7");
        ArchivedPageInfo noArchivedPageInfo = caller.findArchivedPageInfo(dto2);

        // 페이지가 아카이빙되어 있지 않은 경우
        assertThat(caller.isArchived(noArchivedPageInfo)).isFalse();

        fakeWebArchiveServer.stop();
    }

    @DisplayName("찾으려는 페이지가 web archive 서버에 아카이빙되어 있는지 확인할 수 있다")
    @Test
    public void check_if_a_page_to_find_is_archived_in_web_archive_server() {
        WebArchiveApiCaller caller = new WebArchiveApiCaller("http://localhost:8080",
            "/wayback/available?url={url}&timestamp={timestamp}",
            CustomRestTemplateBuilder.get());
        ArchivedPageInfo pageInfo = ArchivedPageInfoBuilder.builder()
            .withAccessibleArchivedSnapshots()
            .build();

        assertThat(caller.isArchived(pageInfo)).isTrue();

        ArchivedPageInfo noPageInfo = ArchivedPageInfoBuilder.builder()
            .withEmptyArchivedSnapshots()
            .build();

        assertThat(caller.isArchived(noPageInfo)).isFalse();
    }

    @DisplayName("web archive 응답 json 데이터를 ArchivedPageInfo 타입 객체로 역직렬화할 수 있다")
    @Test
    public void deserialize_response_from_web_archive_server() throws JsonProcessingException {
        WebArchiveApiCaller caller = new WebArchiveApiCaller("http://localhost:8080",
            "/wayback/available?url={url}&timestamp={timestamp}",
            CustomRestTemplateBuilder.get());

        FakeWebArchiveServer fakeWebArchiveServer = new FakeWebArchiveServer();
        fakeWebArchiveServer.respondItHasArchivedPage();
        fakeWebArchiveServer.start();

        CheckPostArchivedDto dto = new CheckPostArchivedDto("2021", "3");
        Assertions.assertThat(caller.findArchivedPageInfo(dto))
            .isInstanceOf(ArchivedPageInfo.class);

        fakeWebArchiveServer.stop();
    }

    @DisplayName("url을 인코딩할 수 있다")
    @Test
    public void encode_simple_url() {
        String rootUri = "http://archive.org";
        String checkArchivedUri = "/wayback/available?url={url}&timestamp={timestamp}";
        String uri = UriComponentsBuilder.fromUriString(rootUri)
            .uriComponents(
                UriComponentsBuilder.fromUriString(checkArchivedUri)
                    .buildAndExpand("https://x", "20240101")
            )
            .build().toString();

        Assertions.assertThat(uri).isEqualTo(
            "http://archive.org/wayback/available?url=https://x&timestamp=20240101"
        );
    }
}
