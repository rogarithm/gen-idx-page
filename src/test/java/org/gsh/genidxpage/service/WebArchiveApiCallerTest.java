package org.gsh.genidxpage.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.gsh.genidxpage.FakeWebArchiveServer;
import org.gsh.genidxpage.config.CustomRestTemplateBuilder;
import org.gsh.genidxpage.service.dto.ArchivedPageInfo;
import org.gsh.genidxpage.service.dto.CheckPostArchivedDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class WebArchiveApiCallerTest {

    @DisplayName("찾으려는 페이지가 web archive 서버에 어떻게 아카이빙되어 있는지 확인할 수 있다")
    @Test
    public void find_how_a_page_is_archived_in_web_archive_server() {
        WebArchiveApiCaller caller = new WebArchiveApiCaller("http://localhost:8080", CustomRestTemplateBuilder.get());

        FakeWebArchiveServer fakeWebArchiveServer = new FakeWebArchiveServer();
        fakeWebArchiveServer.respondItHasArchivedPage();
        fakeWebArchiveServer.start();

        CheckPostArchivedDto dto = new CheckPostArchivedDto("2021", "3");
        ArchivedPageInfo archivedPageInfo = caller.findArchivedPageInfo(
            "/wayback/available?url={url}&timestamp={timestamp}",
            dto
        );

        assertThat(archivedPageInfo.accessibleUrl().contains("2021/03")).isTrue();

        fakeWebArchiveServer.respondItHasNoArchivedPage();
        ArchivedPageInfo noArchivedPageInfo = caller.findArchivedPageInfo(
            "/wayback/available?url={url}&timestamp={timestamp}",
            dto
        );
        assertThat(caller.isArchived(noArchivedPageInfo)).isFalse();

        fakeWebArchiveServer.stop();
    }

    @DisplayName("찾으려는 페이지가 web archive 서버에 아카이빙되어 있는지 확인할 수 있다")
    @Test
    public void check_if_a_page_to_find_is_archived_in_web_archive_server() {
        WebArchiveApiCaller caller = new WebArchiveApiCaller("http://localhost:8080", CustomRestTemplateBuilder.get());
        ArchivedPageInfo mockedPageInfo = mock(ArchivedPageInfo.class);
        when(mockedPageInfo.isAccessible()).thenReturn(true);

        assertThat(caller.isArchived(mockedPageInfo)).isTrue();

        ArchivedPageInfo mockedNoPageInfo = mock(ArchivedPageInfo.class);
        when(mockedNoPageInfo.isAccessible()).thenReturn(false);

        assertThat(caller.isArchived(mockedNoPageInfo)).isFalse();
    }

    @DisplayName("isArchived() 호출 결과를 ArchivedPageInfo 타입 객체로 역직렬화할 수 있다")
    @Test
    public void deserialize_response_of_isArchived() throws JsonProcessingException {
        String responseOfIsArchived = """
            {
              "url": "agile.egloos.com/archives/2021/03",
              "archived_snapshots": {
                "closest": {
                  "status": "200",
                  "available": true,
                  "url": "http://web.archive.org/web/20230614220926/http://agile.egloos.com/archives/2021/03",
                  "timestamp": "20230614220926"
                }
              },
              "timestamp": "20240101"
            }
            """;
        ObjectMapper mapper = new ObjectMapper();
        ArchivedPageInfo obj = mapper.readValue(
            responseOfIsArchived,
            ArchivedPageInfo.class
        );

        Assertions.assertThat(obj.getUrl()).isEqualTo("agile.egloos.com/archives/2021/03");
        Assertions.assertThat(obj.getArchivedSnapshots()).isNotNull();
    }
}