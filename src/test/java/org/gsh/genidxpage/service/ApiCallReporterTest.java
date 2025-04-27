package org.gsh.genidxpage.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.assertj.core.api.Assertions;
import org.gsh.genidxpage.dao.WebArchiveReportMapper;
import org.gsh.genidxpage.entity.ArchivedPageUrlReport;
import org.gsh.genidxpage.service.dto.CheckPostArchivedDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

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
}
