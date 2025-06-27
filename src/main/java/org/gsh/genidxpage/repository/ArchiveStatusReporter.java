package org.gsh.genidxpage.repository;

import org.gsh.genidxpage.dao.ArchiveStatusMapper;
import org.gsh.genidxpage.entity.ArchiveStatus;
import org.gsh.genidxpage.entity.PostGroupType;
import org.gsh.genidxpage.service.PostGroupTypeResolver;
import org.gsh.genidxpage.service.dto.CheckPostArchived;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ArchiveStatusReporter {

    private final ArchiveStatusMapper reportMapper;
    private final PostGroupTypeResolver resolver;

    public ArchiveStatusReporter(ArchiveStatusMapper reportMapper, PostGroupTypeResolver resolver) {
        this.reportMapper = reportMapper;
        this.resolver = resolver;
    }

    public void reportArchivedPageSearch(final CheckPostArchived dto, final Boolean pageExists) {
        String groupKey = dto.getGroupKey();
        PostGroupType postGroupType = resolver.resolve(groupKey);
        Long postGroupTypeId = postGroupType.getId();

        ArchiveStatus hasReport = reportMapper.selectByGroupKey(groupKey);

        if (hasReport != null) {
            reportMapper.update(ArchiveStatus.updateFrom(postGroupTypeId, hasReport, pageExists));
            return;
        }

        ArchiveStatus report = ArchiveStatus.createFrom(dto, pageExists, postGroupTypeId);
        reportMapper.insert(report);
    }

    public boolean hasArchivedPage(final CheckPostArchived dto) {
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
