package org.gsh.genidxpage.service;

import org.gsh.genidxpage.dao.WebArchiveReportMapper;
import org.gsh.genidxpage.entity.ArchivedPageUrlReport;
import org.gsh.genidxpage.service.dto.CheckPostArchivedDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApiCallReporter {

    private final WebArchiveReportMapper reportMapper;

    public ApiCallReporter(WebArchiveReportMapper reportMapper) {
        this.reportMapper = reportMapper;
    }

    void reportArchivedPageSearch(final CheckPostArchivedDto dto, final Boolean pageExists) {
        ArchivedPageUrlReport hasReport = reportMapper.selectReportByYearMonth(dto.getYear(),
            dto.getMonth());

        if (hasReport != null) {
            reportMapper.updateReport(ArchivedPageUrlReport.from(dto, pageExists));
            return;
        }

        ArchivedPageUrlReport report = ArchivedPageUrlReport.from(dto, pageExists);
        reportMapper.insertReport(report);
    }

    public boolean hasArchivedPage(final CheckPostArchivedDto dto) {
        ArchivedPageUrlReport report = reportMapper.selectReportByYearMonth(
            dto.getYear(),
            dto.getMonth()
        );

        return report.getPageExists() == Boolean.TRUE;
    }

    List<String> readAllFailedRequestInput() {
        return reportMapper.selectByPageExists(Boolean.FALSE)
            .stream()
            .map(report -> report.getYear() + "/" + report.getMonth())
            .toList();
    }
}
