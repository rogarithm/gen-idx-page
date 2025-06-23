package org.gsh.genidxpage.repository;

import org.gsh.genidxpage.dao.ArchiveStatusMapper;
import org.gsh.genidxpage.entity.ArchiveStatus;
import org.gsh.genidxpage.service.dto.CheckPostArchivedDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ArchiveStatusReporter {

    private final ArchiveStatusMapper reportMapper;

    public ArchiveStatusReporter(ArchiveStatusMapper reportMapper) {
        this.reportMapper = reportMapper;
    }

    public void reportArchivedPageSearch(final CheckPostArchivedDto dto, final Boolean pageExists) {
        ArchiveStatus hasReport = reportMapper.selectByGroupKey(dto.getGroupKey());

        if (hasReport != null) {
            reportMapper.update(ArchiveStatus.updateFrom(hasReport, pageExists));
            return;
        }

        ArchiveStatus report = ArchiveStatus.createFrom(dto, pageExists);
        reportMapper.insert(report);
    }

    public boolean hasArchivedPage(final CheckPostArchivedDto dto) {
        ArchiveStatus report = reportMapper.selectByGroupKey(dto.getGroupKey());

        return report.getPageExists() == Boolean.TRUE;
    }

    public List<String> readAllFailedRequestInput() {
        return reportMapper.selectAllFailed()
            .stream()
            .map(ArchiveStatus::getGroupKey)
            .toList();
    }
}
