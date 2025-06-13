package org.gsh.genidxpage.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.gsh.genidxpage.repository.ArchiveStatusReporter;
import org.gsh.genidxpage.repository.PostListPageRecorder;
import org.gsh.genidxpage.repository.PostRecorder;
import org.gsh.genidxpage.service.dto.ArchivedPageInfo;
import org.gsh.genidxpage.service.dto.ArchivedPageInfoBuilder;
import org.gsh.genidxpage.service.dto.CheckPostArchivedDto;
import org.gsh.genidxpage.service.dto.UnreachableArchivedPageInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

class ArchivePageServiceTest {

    @DisplayName("페이지 아키이빙 정보를 찾지 못했을 때 db에 기록한다")
    @Test
    public void write_to_db_when_page_archive_info_not_found() {
        ArchivedPageInfo noArchivedPageInfo = ArchivedPageInfoBuilder.builder()
            .withEmptyArchivedSnapshots()
            .build();
        WebArchiveApiCaller caller = mock(WebArchiveApiCaller.class);
        when(caller.findArchivedPageInfo(any())).thenReturn(
            noArchivedPageInfo
        );
        ArchiveStatusReporter reporter = mock(ArchiveStatusReporter.class);

        AgileStoryArchivePageService service = new AgileStoryArchivePageService(caller, reporter, null, null, null);

        CheckPostArchivedDto dto = new CheckPostArchivedDto("1999", "7");
        service.findArchivedPageInfo(dto);

        verify(reporter).reportArchivedPageSearch(any(CheckPostArchivedDto.class), eq(Boolean.FALSE));
    }

    @DisplayName("타임아웃 초과로 페이지 아카이빙 정보를 읽어오지 못했을 때 db에 기록한다")
    @Test
    public void write_to_db_when_page_archive_info_read_timeout() {
        WebArchiveApiCaller caller = mock(WebArchiveApiCaller.class);
        when(caller.findArchivedPageInfo(any())).thenReturn(
            new UnreachableArchivedPageInfo()
        );
        ArchiveStatusReporter reporter = mock(ArchiveStatusReporter.class);

        AgileStoryArchivePageService service = new AgileStoryArchivePageService(caller, reporter,
            null, null, null);

        CheckPostArchivedDto dto = new CheckPostArchivedDto("2020", "3");
        service.findArchivedPageInfo(dto);

        verify(reporter).reportArchivedPageSearch(any(CheckPostArchivedDto.class),
            eq(Boolean.FALSE));
    }

    @DisplayName("페이지 아키이빙 정보를 찾았을 때 db에 기록한다")
    @Test
    public void write_to_db_when_page_archive_info_found() {
        ArchivedPageInfo archivedPageInfo = ArchivedPageInfoBuilder.builder()
            .withAccessibleArchivedSnapshots()
            .build();

        WebArchiveApiCaller caller = mock(WebArchiveApiCaller.class);
        when(caller.findArchivedPageInfo(any())).thenReturn(
            archivedPageInfo
        );
        when(caller.isArchived(any())).thenReturn(true);
        ArchiveStatusReporter reporter = mock(ArchiveStatusReporter.class);

        AgileStoryArchivePageService service = new AgileStoryArchivePageService(caller, reporter,
            mock(PostListPageRecorder.class), null, null);

        CheckPostArchivedDto dto = new CheckPostArchivedDto("2021", "3");
        service.findArchivedPageInfo(dto);

        verify(reporter).reportArchivedPageSearch(any(CheckPostArchivedDto.class), eq(Boolean.TRUE));
    }

    @DisplayName("페이지 아키이빙 정보를 찾았을 때, 접근 url을 db에 기록한다")
    @Test
    public void write_access_url_to_db_when_page_archive_info_found() {
        WebArchiveApiCaller caller = mock(WebArchiveApiCaller.class);
        respondsValidBlogPostPage(caller);

        PostListPageRecorder listPageRecorder = mock(PostListPageRecorder.class);
        AgileStoryArchivePageService service = new AgileStoryArchivePageService(caller,
            mock(ArchiveStatusReporter.class), listPageRecorder, mock(PostRecorder.class),
            mock(WebPageParser.class));

        CheckPostArchivedDto dto = new CheckPostArchivedDto("2021", "3");
        service.findBlogPageLink(dto);

        verify(listPageRecorder).record(any(CheckPostArchivedDto.class), any(ArchivedPageInfo.class));
    }

    @DisplayName("블로그 글 목록 페이지로부터 파싱한 블로그 링크 목록을 db에 기록한다")
    @Test
    public void write_post_link_parsed_from_list_page_to_db() {
        PostRecorder postRecorder = mock(PostRecorder.class);
        WebArchiveApiCaller caller = mock(WebArchiveApiCaller.class);
        respondsValidBlogPostPage(caller);

        AgileStoryArchivePageService service = new AgileStoryArchivePageService(
            caller,
            mock(ArchiveStatusReporter.class),
            mock(PostListPageRecorder.class),
            postRecorder,
            mock(WebPageParser.class)
        );

        CheckPostArchivedDto dto = new CheckPostArchivedDto("2021", "3");
        service.findBlogPageLink(dto);

        verify(postRecorder).record(any(), any());
    }

    private void respondsValidBlogPostPage(WebArchiveApiCaller caller) {
        ArchivedPageInfo archivedPageInfo = ArchivedPageInfoBuilder.builder()
            .withAccessibleArchivedSnapshots()
            .build();

        when(caller.findArchivedPageInfo(any())).thenReturn(
            archivedPageInfo
        );
        when(caller.isArchived(any())).thenReturn(true);
        when(caller.findBlogPostPage(any())).thenReturn(ResponseEntity.ok()
            .body("<div><a href=\"link\">title</a></div>"));
    }

    @DisplayName("실패한 요청 정보를 db로부터 읽어온다")
    @Test
    public void read_all_failed_request_info_from_db() {
        ArchiveStatusReporter reporter = mock(ArchiveStatusReporter.class);
        AgileStoryArchivePageService service = new AgileStoryArchivePageService(
            mock(WebArchiveApiCaller.class),
            reporter,
            mock(PostListPageRecorder.class),
            mock(PostRecorder.class),
            null
        );

        service.findFailedRequests();

        verify(reporter).readAllFailedRequestInput();
    }
}
