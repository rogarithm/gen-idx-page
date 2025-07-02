package org.gsh.genidxpage.repository;

import org.gsh.genidxpage.dao.ArchiveStatusMapper;
import org.gsh.genidxpage.entity.ArchiveStatus;
import org.gsh.genidxpage.entity.PostGroupType;
import org.gsh.genidxpage.service.PostGroupTypeResolver;
import org.gsh.genidxpage.vo.GroupKey;
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

    public void reportArchivedPageSearch(final GroupKey groupKey, final Boolean pageExists) {
        PostGroupType postGroupType = resolver.resolve(groupKey);
        Long postGroupTypeId = postGroupType.getId();

        String value = groupKey.value();
        ArchiveStatus hasReport = reportMapper.selectByGroupKey(value);

        if (hasReport != null) {
            reportMapper.update(ArchiveStatus.updateFrom(postGroupTypeId, hasReport, pageExists));
            return;
        }

        ArchiveStatus report = ArchiveStatus.createFrom(postGroupTypeId, value, pageExists);
        reportMapper.insert(report);
    }

    public boolean hasArchivedPage(final String groupKey) {
        ArchiveStatus report = reportMapper.selectByGroupKey(groupKey);

        return report.getPageExists() == Boolean.TRUE;
    }

    public List<String> readAllFailedRequestInput() {
        return reportMapper.selectAllFailed()
            .stream()
            .map(ArchiveStatus::getGroupKey)
            .toList();
    }
}
