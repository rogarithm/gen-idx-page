package org.gsh.genidxpage.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.gsh.genidxpage.repository.ArchiveStatusReporter;
import org.gsh.genidxpage.repository.PostListPageRecorder;
import org.gsh.genidxpage.repository.PostRecorder;
import org.gsh.genidxpage.service.dto.ArchivedPageInfo;
import org.gsh.genidxpage.service.dto.ArchivedPageInfoBuilder;
import org.gsh.genidxpage.service.dto.CheckCategoryPostArchivedDto;
import org.gsh.genidxpage.service.dto.CheckPostArchived;
import org.gsh.genidxpage.service.dto.CheckYearMonthPostArchivedDto;
import org.gsh.genidxpage.service.dto.UnreachableArchivedPageInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.IntStream;

class ArchivePageServiceTest {

    WebArchiveApiCaller caller;
    ArchiveStatusReporter reporter;
    PostListPageRecorder listPageRecorder;
    PostRecorder postRecorder;
    WebPageParser parser;

    @BeforeEach
    public void setUp() {
        caller = mock(WebArchiveApiCaller.class);
        reporter = mock(ArchiveStatusReporter.class);
        listPageRecorder = mock(PostListPageRecorder.class);
        postRecorder = mock(PostRecorder.class);
        parser = mock(WebPageParser.class);
    }

    @DisplayName("페이지 아키이빙 정보를 찾지 못했을 때 db에 기록한다")
    @Test
    public void write_to_db_when_page_archive_info_not_found() {
        ArchivedPageInfo noArchivedPageInfo = ArchivedPageInfoBuilder.builder()
            .withEmptyArchivedSnapshots()
            .build();
        when(caller.findArchivedPageInfo(any(), any())).thenReturn(
            noArchivedPageInfo
        );

        List<ArchivePageService> services = List.of(
            new AgileStoryArchivePageService(caller, reporter, null, null, null),
            new AeternumArchivePageService(caller, reporter, null, null, null)
        );
        List<CheckPostArchived> dtos = List.of(
            new CheckYearMonthPostArchivedDto("1999/07"),
            new CheckCategoryPostArchivedDto("Domain-Driven Design")
        );

        IntStream.range(0, services.size()).forEach(idx -> {
            ArchivePageService service = services.get(idx);
            CheckPostArchived dto = dtos.get(idx);
            service.findArchivedPageInfo(dto);
        });

        verify(reporter, times(services.size())).reportArchivedPageSearch(any(), eq(Boolean.FALSE));
    }

    @DisplayName("타임아웃 초과로 페이지 아카이빙 정보를 읽어오지 못했을 때 db에 기록한다")
    @Test
    public void write_to_db_when_page_archive_info_read_timeout() {
        when(caller.findArchivedPageInfo(any(), any())).thenReturn(
            new UnreachableArchivedPageInfo()
        );

        List<ArchivePageService> services = List.of(
            new AgileStoryArchivePageService(caller, reporter, null, null, null),
            new AeternumArchivePageService(caller, reporter, null, null, null)
        );
        List<CheckPostArchived> dtos = List.of(
            new CheckYearMonthPostArchivedDto("2020/03"),
            new CheckCategoryPostArchivedDto("///")
        );

        IntStream.range(0, services.size()).forEach(idx -> {
            ArchivePageService service = services.get(idx);
            CheckPostArchived dto = dtos.get(idx);
            service.findArchivedPageInfo(dto);
        });

        verify(reporter, times(services.size())).reportArchivedPageSearch(any(), eq(Boolean.FALSE));
    }

    @DisplayName("페이지 아키이빙 정보를 찾았을 때 db에 기록한다")
    @Test
    public void write_to_db_when_page_archive_info_found() {
        ArchivedPageInfo archivedPageInfo = ArchivedPageInfoBuilder.builder()
            .withAccessibleArchivedSnapshots()
            .build();

        when(caller.findArchivedPageInfo(any(), any())).thenReturn(
            archivedPageInfo
        );
        when(caller.isArchived(any())).thenReturn(true);

        List<ArchivePageService> services = List.of(
            new AgileStoryArchivePageService(caller, reporter, listPageRecorder, null, null),
            new AeternumArchivePageService(caller, reporter, listPageRecorder, null, null)
        );
        List<CheckPostArchived> dtos = List.of(
            new CheckYearMonthPostArchivedDto("2021/03"),
            new CheckCategoryPostArchivedDto("///")
        );

        IntStream.range(0, services.size()).forEach(idx -> {
            ArchivePageService service = services.get(idx);
            CheckPostArchived dto = dtos.get(idx);
            service.findArchivedPageInfo(dto);
        });

        verify(reporter, times(services.size())).reportArchivedPageSearch(any(), eq(Boolean.TRUE));
    }

    @DisplayName("페이지 아키이빙 정보를 찾았을 때, 접근 url을 db에 기록한다")
    @Test
    public void write_access_url_to_db_when_page_archive_info_found() {
        respondsValidBlogPostPage(caller);

        List<ArchivePageService> services = List.of(
            new AgileStoryArchivePageService(caller, reporter, listPageRecorder, postRecorder,
                parser),
            new AeternumArchivePageService(caller, reporter, listPageRecorder, postRecorder, parser)
        );
        List<CheckPostArchived> dtos = List.of(
            new CheckYearMonthPostArchivedDto("2021/03"),
            new CheckCategoryPostArchivedDto("Domain-Driven Design")
        );

        IntStream.range(0, services.size()).forEach(idx -> {
            ArchivePageService service = services.get(idx);
            CheckPostArchived dto = dtos.get(idx);
            service.findBlogPageLink(dto);
        });

        verify(listPageRecorder, times(services.size())).record(any(), any(ArchivedPageInfo.class));
    }

    @DisplayName("블로그 글 목록 페이지로부터 파싱한 블로그 링크 목록을 db에 기록한다")
    @Test
    public void write_post_link_parsed_from_list_page_to_db() {
        respondsValidBlogPostPage(caller);

        List<ArchivePageService> services = List.of(
            new AgileStoryArchivePageService(caller, reporter, listPageRecorder, postRecorder,
                parser),
            new AeternumArchivePageService(caller, reporter, listPageRecorder, postRecorder, parser)
        );
        List<CheckPostArchived> dtos = List.of(
            new CheckYearMonthPostArchivedDto("2021/03"),
            new CheckCategoryPostArchivedDto("Domain-Driven Design")
        );

        IntStream.range(0, services.size()).forEach(idx -> {
            ArchivePageService service = services.get(idx);
            CheckPostArchived dto = dtos.get(idx);
            service.findBlogPageLink(dto);
        });

        verify(postRecorder, times(services.size())).record(any(), any());
    }

    private void respondsValidBlogPostPage(WebArchiveApiCaller caller) {
        ArchivedPageInfo archivedPageInfo = ArchivedPageInfoBuilder.builder()
            .withAccessibleArchivedSnapshots()
            .build();

        when(caller.findArchivedPageInfo(any(), any())).thenReturn(
            archivedPageInfo
        );
        when(caller.isArchived(any())).thenReturn(true);
        when(caller.findBlogPostPage(any())).thenReturn(ResponseEntity.ok()
            .body("<div><a href=\"link\">title</a></div>"));
    }

    @DisplayName("실패한 요청 정보를 db로부터 읽어온다")
    @Test
    public void read_all_failed_request_info_from_db() {
        List<ArchivePageService> services = List.of(
            new AgileStoryArchivePageService(caller, reporter, listPageRecorder, postRecorder,
                null),
            new AeternumArchivePageService(caller, reporter, listPageRecorder, postRecorder, null)
        );

        IntStream.range(0, services.size()).forEach(idx -> {
            ArchivePageService service = services.get(idx);
            service.findFailedRequests();
        });

        verify(reporter, times(services.size())).readAllFailedRequestInput();
    }
}
