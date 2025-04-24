package org.gsh.genidxpage.service;

import org.gsh.genidxpage.service.dto.CheckPostArchivedDto;

public interface ArchivePageService {

    String findBlogPageLink(final CheckPostArchivedDto dto);
}
