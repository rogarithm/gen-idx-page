package org.gsh.genidxpage.service;

import org.gsh.genidxpage.service.dto.ArchivedPageInfo;
import org.gsh.genidxpage.service.dto.CheckPostArchived;

import java.util.List;

public interface ArchivePageService {

    String findBlogPageLink(CheckPostArchived dto);

    List<String> findFailedRequests();

    ArchivedPageInfo findArchivedPageInfo(CheckPostArchived dto);
}
