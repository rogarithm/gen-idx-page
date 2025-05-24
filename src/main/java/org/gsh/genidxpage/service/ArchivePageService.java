package org.gsh.genidxpage.service;

import org.gsh.genidxpage.service.dto.CheckPostArchivedDto;

import java.util.List;

public interface ArchivePageService {

    String findBlogPageLink(CheckPostArchivedDto dto);

    List<String> findFailedRequests();
}
