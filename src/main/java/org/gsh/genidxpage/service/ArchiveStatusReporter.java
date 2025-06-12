package org.gsh.genidxpage.service;

import org.gsh.genidxpage.dao.ArchiveStatusMapper;
import org.gsh.genidxpage.entity.ArchiveStatus;
import org.gsh.genidxpage.service.dto.CheckPostArchivedDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ArchiveStatusReporter {

    private final ArchiveStatusMapper reportMapper;

    public ArchiveStatusReporter(ArchiveStatusMapper reportMapper) {
        this.reportMapper = reportMapper;
    }

    void reportArchivedPageSearch(final CheckPostArchivedDto dto, final Boolean pageExists) {
        ArchiveStatus hasReport = reportMapper.selectByYearMonth(dto.getYear(),
            dto.getMonth());

        if (hasReport != null) {
            reportMapper.update(ArchiveStatus.updateFrom(hasReport, pageExists));
            return;
        }

        ArchiveStatus report = ArchiveStatus.createFrom(dto, pageExists);
        reportMapper.insert(report);
    }

    public boolean hasArchivedPage(final CheckPostArchivedDto dto) {
        ArchiveStatus report = reportMapper.selectByYearMonth(
            dto.getYear(),
            dto.getMonth()
        );

        return report.getPageExists() == Boolean.TRUE;
    }

    List<String> readAllFailedRequestInput() {
        return reportMapper.selectAllFailed()
            .stream()
            .map(report -> report.getYear() + "/" + report.getMonth())
            .toList();
    }
}
