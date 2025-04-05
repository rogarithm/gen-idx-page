package org.gsh.genidxpage.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.gsh.genidxpage.FakeWebArchiveServer;
import org.gsh.genidxpage.service.dto.CheckPostArchivedDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class WebArchiveApiCallerTest {

    @DisplayName("찾으려는 페이지가 web archive 서버에 아카이빙되어 있는지 확인할 수 있다")
    @Test
    public void check_if_a_page_to_find_is_archived_in_web_archive_server() {
        WebArchiveApiCaller caller = new WebArchiveApiCaller("http://localhost:8080");

        FakeWebArchiveServer fakeWebArchiveServer = new FakeWebArchiveServer();

        fakeWebArchiveServer.respondItHasArchivedPage();

        fakeWebArchiveServer.start();

        CheckPostArchivedDto dto = new CheckPostArchivedDto("2021", "3");
        //TODO isArchived에서 json 파싱한 결과를 가지고 아카이빙된 글이 있는지 확인해야 한다
        boolean isArchived = caller.isArchived("/wayback/available?url={url}&timestamp={timestamp}", dto);
        assertThat(isArchived).isTrue();

        fakeWebArchiveServer.stop();
    }
}