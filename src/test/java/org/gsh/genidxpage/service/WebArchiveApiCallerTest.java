package org.gsh.genidxpage.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.gsh.genidxpage.FakeWebArchiveServer;
import org.gsh.genidxpage.config.CustomRestTemplateBuilder;
import org.gsh.genidxpage.service.dto.ArchivedPageInfo;
import org.gsh.genidxpage.service.dto.ArchivedPageInfoBuilder;
import org.gsh.genidxpage.service.dto.CheckPostArchived;
import org.gsh.genidxpage.service.dto.CheckYearMonthPostArchivedDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

        CheckPostArchived dto = new CheckYearMonthPostArchivedDto("2021/03");
        ArchivedPageInfo archivedPageInfo = caller.findArchivedPageInfo(dto.getUrl(),
            dto.getTimestamp());

        // 페이지가 아카이빙되어 있는 경우
        assertThat(caller.isArchived(archivedPageInfo)).isTrue();

        fakeWebArchiveServer.respondItHasNoArchivedPage();
        CheckPostArchived dto2 = new CheckYearMonthPostArchivedDto("1999/07");
        ArchivedPageInfo noArchivedPageInfo = caller.findArchivedPageInfo(dto2.getUrl(),
            dto2.getTimestamp());

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

        CheckPostArchived dto = new CheckYearMonthPostArchivedDto("2021/03");
        assertThat(caller.findArchivedPageInfo(dto.getUrl(), dto.getTimestamp()))
            .isInstanceOf(ArchivedPageInfo.class);

        fakeWebArchiveServer.stop();
    }

    @DisplayName("url을 인코딩할 수 있다")
    @Test
    public void encode_simple_url() {
        WebArchiveApiCaller caller = new WebArchiveApiCaller("http://localhost:8080",
            "/wayback/available?url={url}&timestamp={timestamp}",
            CustomRestTemplateBuilder.get());

        CheckPostArchived dto = new CheckYearMonthPostArchivedDto("2023/01");
        assertThat(caller.buildUri(dto.getUrl(), dto.getTimestamp())).matches(
            "http://localhost:8080/wayback/available\\?url=https://agile.egloos.com/archives/2023/01&timestamp=\\d{8}"
        );
    }

    @DisplayName("timestamp 쿼리 파리미터 여부에 따라 동적으로 uri를 완성한다")
    @Test
    public void build_uri_dynamically_depending_on_given_query_parameters() {
        WebArchiveApiCaller callerWithTimestamp = new WebArchiveApiCaller("http://localhost:8080",
            "/wayback/available?url={url}&timestamp={timestamp}",
            CustomRestTemplateBuilder.get());
        CheckPostArchived dto = new CheckYearMonthPostArchivedDto("2021/03");

        assertThat(callerWithTimestamp.buildUri(dto.getUrl(), dto.getTimestamp())).matches(
            "http://localhost:8080/wayback/available\\?url=[^&]*&timestamp=\\d{8}"
        );

        WebArchiveApiCaller callerWithoutTimestamp = new WebArchiveApiCaller("http://localhost:8080",
            "/wayback/available?url={url}",
            CustomRestTemplateBuilder.get());
        assertThat(callerWithoutTimestamp.buildUri(dto.getUrl(), dto.getTimestamp())).matches(
            "http://localhost:8080/wayback/available\\?url=[^&]*"
        );
    }
}
