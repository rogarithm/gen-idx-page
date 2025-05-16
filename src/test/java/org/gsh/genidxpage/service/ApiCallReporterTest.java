package org.gsh.genidxpage.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.assertj.core.api.Assertions;
import org.gsh.genidxpage.dao.WebArchiveReportMapper;
import org.gsh.genidxpage.entity.ArchivedPageUrlReport;
import org.gsh.genidxpage.service.dto.CheckPostArchivedDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

class ApiCallReporterTest {

    @DisplayName("web archive로부터 url을 받아온 연월인지 확인할 수 있다")
    @Test
    public void check_url_exists_in_web_archive_for_given_year_month() {
        WebArchiveReportMapper mapper = mock(WebArchiveReportMapper.class);
        ApiCallReporter reporter = new ApiCallReporter(mapper);
        ArchivedPageUrlReport report = new ArchivedPageUrlReport(
            "2021", "3", Boolean.TRUE, LocalDateTime.now()
        );

        when(mapper.selectReportByYearMonth(any(), any())).thenReturn(report);

        boolean hasArchivedPage = reporter.hasArchivedPage(
            new CheckPostArchivedDto("2021", "3")
        );
        Assertions.assertThat(hasArchivedPage).isTrue();
    }

    @DisplayName("이미 db에 기록된 경우, 새로운 행을 추가하지 않는다")
    @Test
    public void only_update_status_when_page_status_already_inserted() {
        WebArchiveReportMapper mapper = mock(WebArchiveReportMapper.class);
        ApiCallReporter reporter = new ApiCallReporter(mapper);
        ArchivedPageUrlReport report = new ArchivedPageUrlReport(
            "2021", "3", Boolean.TRUE, LocalDateTime.now()
        );

        when(mapper.selectReportByYearMonth(any(), any())).thenReturn(
            report);
        doNothing().when(mapper).updateReport(report);

        CheckPostArchivedDto dto = new CheckPostArchivedDto("2021", "3");
        reporter.reportArchivedPageSearch(dto, Boolean.TRUE);

        verify(mapper).selectReportByYearMonth(any(), any());
        verify(mapper).updateReport(any(ArchivedPageUrlReport.class));
    }

    @DisplayName("실패한 요청 정보를 db로부터 읽어온다")
    @Test
    public void read_all_failed_request_info_from_db() {
        WebArchiveReportMapper mapper = mock(WebArchiveReportMapper.class);
        ApiCallReporter reporter = new ApiCallReporter(mapper);
        List<ArchivedPageUrlReport> failRequestReports = List.of(
            new ArchivedPageUrlReport("2020", "05", Boolean.FALSE, LocalDateTime.now()),
            new ArchivedPageUrlReport("2021", "03", Boolean.FALSE, LocalDateTime.now())
        );

        when(mapper.selectByPageExists(any())).thenReturn(failRequestReports);

        Assertions.assertThat(reporter.readAllFailedRequestInput())
            .isEqualTo(List.of("2020/05", "2021/03"));

        verify(mapper).selectByPageExists(any());
    }
}
