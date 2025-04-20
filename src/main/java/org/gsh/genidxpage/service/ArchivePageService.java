package org.gsh.genidxpage.service;

import org.gsh.genidxpage.common.exception.ErrorCode;
import org.gsh.genidxpage.exception.ArchivedPageNotFoundExceptioin;
import org.gsh.genidxpage.service.dto.ArchivedPageInfo;
import org.gsh.genidxpage.service.dto.CheckPostArchivedDto;
import org.springframework.http.ResponseEntity;

public class ArchivePageService {

    public ArchivePageService(WebArchiveApiCaller webArchiveApiCaller) {
        this.webArchiveApiCaller = webArchiveApiCaller;
    }

    private final WebArchiveApiCaller webArchiveApiCaller;

    public ArchivedPageInfo findArchivedPageInfo(final CheckPostArchivedDto dto) {
        ArchivedPageInfo archivedPageInfo = webArchiveApiCaller.findArchivedPageInfo(dto);

        if (!webArchiveApiCaller.isArchived(archivedPageInfo)) {
            throw new ArchivedPageNotFoundExceptioin(ErrorCode.BAD_REQUEST, "resource not found");
        }

        return archivedPageInfo;
    }

    public String findBlogPostPage(ArchivedPageInfo archivedPageInfo) {
        ResponseEntity<String> blogPostResponse = webArchiveApiCaller.findBlogPostPage(
            archivedPageInfo);
        return blogPostResponse.getBody();
    }
}
