package org.gsh.genidxpage.repository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.assertj.core.api.Assertions;
import org.gsh.genidxpage.dao.ArchiveStatusMapper;
import org.gsh.genidxpage.entity.ArchiveStatus;
import org.gsh.genidxpage.entity.ArchiveStatusBuilder;
import org.gsh.genidxpage.service.dto.CheckPostArchivedDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

class ArchiveStatusReporterTest {

    @DisplayName("web archive로부터 url을 받아온 연월인지 확인할 수 있다")
    @Test
    public void check_url_exists_in_web_archive_for_given_year_month() {
        ArchiveStatusMapper mapper = mock(ArchiveStatusMapper.class);
        ArchiveStatusReporter reporter = new ArchiveStatusReporter(mapper);
        ArchiveStatus report = ArchiveStatusBuilder.builder()
            .withYearMonth("2021", "3")
            .thatExists()
            .buildAsNew();

        when(mapper.selectByYearMonth(any(), any())).thenReturn(report);

        boolean hasArchivedPage = reporter.hasArchivedPage(
            new CheckPostArchivedDto("2021", "3")
        );
        Assertions.assertThat(hasArchivedPage).isTrue();
    }

    @DisplayName("이미 db에 기록된 경우, 새로운 행을 추가하지 않는다")
    @Test
    public void only_update_status_when_page_status_already_inserted() {
        ArchiveStatusMapper mapper = mock(ArchiveStatusMapper.class);
        ArchiveStatusReporter reporter = new ArchiveStatusReporter(mapper);
        ArchiveStatus report = ArchiveStatusBuilder.builder()
            .withYearMonth("2021", "3")
            .thatExists()
            .buildAsNew();

        when(mapper.selectByYearMonth(any(), any())).thenReturn(
            report);
        doNothing().when(mapper).update(report);

        CheckPostArchivedDto dto = new CheckPostArchivedDto("2021", "3");
        reporter.reportArchivedPageSearch(dto, Boolean.TRUE);

        verify(mapper).selectByYearMonth(any(), any());
        verify(mapper).update(any(ArchiveStatus.class));
    }

    @DisplayName("실패한 요청 정보를 db로부터 읽어온다")
    @Test
    public void read_all_failed_request_info_from_db() {
        ArchiveStatusMapper mapper = mock(ArchiveStatusMapper.class);
        ArchiveStatusReporter reporter = new ArchiveStatusReporter(mapper);
        List<ArchiveStatus> failRequestReports = List.of(
            ArchiveStatusBuilder.builder()
                .withYearMonth("2020", "05")
                .thatNotExists()
                .buildAsNew(),
            ArchiveStatusBuilder.builder()
                .withYearMonth("2021", "03")
                .thatNotExists()
                .buildAsNew()
        );

        when(mapper.selectAllFailed()).thenReturn(failRequestReports);

        Assertions.assertThat(reporter.readAllFailedRequestInput())
            .isEqualTo(List.of("2020/05", "2021/03"));

        verify(mapper).selectAllFailed();
    }
}
