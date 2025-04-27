package org.gsh.genidxpage.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.gsh.genidxpage.exception.ArchivedPageNotFoundExceptioin;
import org.gsh.genidxpage.service.dto.ArchivedPageInfo;
import org.gsh.genidxpage.service.dto.ArchivedPageInfoBuilder;
import org.gsh.genidxpage.service.dto.CheckPostArchivedDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ArchivePageServiceTest {

    @DisplayName("페이지 아키이빙 정보를 찾지 못했을 때 db에 기록한다")
    @Test
    public void write_to_db_when_page_archive_info_not_found() {
        ArchivedPageInfo noArchivedPageInfo = ArchivedPageInfoBuilder.builder()
            .url("")
            .withEmptyArchivedSnapshots()
            .timestamp(null)
            .build();
        CheckPostArchivedDto dto = new CheckPostArchivedDto("1999", "7");

        WebArchiveApiCaller caller = mock(WebArchiveApiCaller.class);
        when(caller.findArchivedPageInfo(any())).thenReturn(
            noArchivedPageInfo
        );
        ApiCallReporter reporter = mock(ApiCallReporter.class);

        AgileStoryArchivePageService service = new AgileStoryArchivePageService(caller, reporter);

        Assertions.assertThrows(ArchivedPageNotFoundExceptioin.class,
            () -> service.findArchivedPageInfo(dto));

        verify(reporter).reportArchivedPageSearch(any(CheckPostArchivedDto.class), eq(Boolean.FALSE));
    }

    @DisplayName("페이지 아키이빙 정보를 찾았을 때 db에 기록한다")
    @Test
    public void write_to_db_when_page_archive_info_found() {
        ArchivedPageInfo archivedPageInfo = ArchivedPageInfoBuilder.builder()
            .url("url")
            .withAccessibleArchivedSnapshots()
            .timestamp("20240101")
            .build();
        CheckPostArchivedDto dto = new CheckPostArchivedDto("2021", "3");

        WebArchiveApiCaller caller = mock(WebArchiveApiCaller.class);
        when(caller.findArchivedPageInfo(any())).thenReturn(
            archivedPageInfo
        );
        when(caller.isArchived(any())).thenReturn(true);
        ApiCallReporter reporter = mock(ApiCallReporter.class);

        AgileStoryArchivePageService service = new AgileStoryArchivePageService(caller, reporter);

        service.findArchivedPageInfo(dto);

        verify(reporter).reportArchivedPageSearch(any(CheckPostArchivedDto.class), eq(Boolean.TRUE));
    }
}
