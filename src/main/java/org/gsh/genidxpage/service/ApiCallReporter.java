package org.gsh.genidxpage.service;

import org.gsh.genidxpage.dao.WebArchiveReportMapper;
import org.gsh.genidxpage.entity.ArchivedPageUrlReport;
import org.gsh.genidxpage.service.dto.CheckPostArchivedDto;
import org.springframework.stereotype.Component;

@Component
public class ApiCallReporter {

    private final WebArchiveReportMapper reportMapper;

    public ApiCallReporter(WebArchiveReportMapper reportMapper) {
        this.reportMapper = reportMapper;
    }

    public void reportArchivedPageSearch(final CheckPostArchivedDto dto, final Boolean pageExists) {
        ArchivedPageUrlReport report = ArchivedPageUrlReport.from(dto, pageExists);
        reportMapper.insertReport(report);
    }
}
