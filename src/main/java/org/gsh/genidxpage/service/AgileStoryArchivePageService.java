package org.gsh.genidxpage.service;

import org.gsh.genidxpage.common.exception.ErrorCode;
import org.gsh.genidxpage.exception.ArchivedPageNotFoundExceptioin;
import org.gsh.genidxpage.service.dto.ArchivedPageInfo;
import org.gsh.genidxpage.service.dto.CheckPostArchivedDto;
import org.gsh.genidxpage.web.response.PostLinkInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgileStoryArchivePageService implements ArchivePageService {

    private final WebArchiveApiCaller webArchiveApiCaller;
    private final ApiCallReporter reporter;

    public AgileStoryArchivePageService(WebArchiveApiCaller webArchiveApiCaller, ApiCallReporter reporter) {
        this.webArchiveApiCaller = webArchiveApiCaller;
        this.reporter = reporter;
    }

    public String findBlogPageLink(final CheckPostArchivedDto dto) {
        ArchivedPageInfo archivedPageInfo = this.findArchivedPageInfo(dto);
        String blogPost = this.findBlogPostPage(archivedPageInfo);
        return this.buildPageLinks(blogPost);
    }

    public ArchivedPageInfo findArchivedPageInfo(final CheckPostArchivedDto dto) {
        ArchivedPageInfo archivedPageInfo = webArchiveApiCaller.findArchivedPageInfo(dto);

        if (!webArchiveApiCaller.isArchived(archivedPageInfo)) {
            reporter.reportArchivedPageSearch(dto, Boolean.FALSE);
            throw new ArchivedPageNotFoundExceptioin(ErrorCode.BAD_REQUEST, "resource not found");
        }

        reporter.reportArchivedPageSearch(dto, Boolean.TRUE);
        return archivedPageInfo;
    }

    public String findBlogPostPage(final ArchivedPageInfo archivedPageInfo) {
        ResponseEntity<String> blogPostResponse = webArchiveApiCaller.findBlogPostPage(
            archivedPageInfo);
        return blogPostResponse.getBody();
    }

    public String buildPageLinks(final String blogPost) {
        WebPageParser webPageParser = new WebPageParser();
        List<PostLinkInfo> postLinks = webPageParser.findPostLinks(blogPost);
        return webPageParser.buildPageLinks(postLinks);
    }
}
