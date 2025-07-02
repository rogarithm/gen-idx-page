package org.gsh.genidxpage.service;

import org.gsh.genidxpage.repository.ArchiveStatusReporter;
import org.gsh.genidxpage.repository.PostListPageRecorder;
import org.gsh.genidxpage.repository.PostRecorder;
import org.gsh.genidxpage.service.dto.ArchivedPageInfo;
import org.gsh.genidxpage.service.dto.CheckPostArchived;
import org.gsh.genidxpage.service.dto.EmptyArchivedPageInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Service
public class AgileStoryArchivePageService implements ArchivePageService {

    private final WebArchiveApiCaller webArchiveApiCaller;
    private final ArchiveStatusReporter reporter;
    private final PostListPageRecorder listPageRecorder;
    private final PostRecorder postRecorder;
    private final WebPageParser webPageParser;

    public AgileStoryArchivePageService(WebArchiveApiCaller webArchiveApiCaller,
        ArchiveStatusReporter reporter, PostListPageRecorder listPageRecorder, PostRecorder postRecorder,
        WebPageParser webPageParser) {
        this.webArchiveApiCaller = webArchiveApiCaller;
        this.reporter = reporter;
        this.listPageRecorder = listPageRecorder;
        this.postRecorder = postRecorder;
        this.webPageParser = webPageParser;
    }

    @Transactional
    @Override
    public String findBlogPageLink(final CheckPostArchived dto) {
        ArchivedPageInfo archivedPageInfo = this.findArchivedPageInfo(dto);
        if (archivedPageInfo.isUnreachable()) {
            log.debug(
                String.format("fail to read blog page link for %s due to timeout",
                    dto.getGroupKey())
            );
            return "";
        }

        if (archivedPageInfo.isEmpty()) {
            log.debug(
                String.format("empty blog page link for %s", dto.getGroupKey()));
            return "";
        }
        Long listPageId = listPageRecorder.record(dto, archivedPageInfo);
        log.debug("id of post list page inserted/updated now is {" + listPageId + "}");

        String blogPost = this.findBlogPostPage(archivedPageInfo);
        postRecorder.record(this.buildPageLinks(blogPost), listPageId);

        return this.buildPageLinks(blogPost);
    }

    @Override
    public List<String> findFailedRequests() {
        return reporter.readAllFailedRequestInput();
    }

    ArchivedPageInfo findArchivedPageInfo(final CheckPostArchived dto) {
        ArchivedPageInfo archivedPageInfo = webArchiveApiCaller.findArchivedPageInfo(dto.getUrl(),
            dto.getTimestamp());

        if (archivedPageInfo.isUnreachable()) {
            reporter.reportArchivedPageSearch(dto.getGroupKey(), Boolean.FALSE);
            return archivedPageInfo;
        }

        if (!webArchiveApiCaller.isArchived(archivedPageInfo)) {
            reporter.reportArchivedPageSearch(dto.getGroupKey(), Boolean.FALSE);
            return new EmptyArchivedPageInfo();
        }

        reporter.reportArchivedPageSearch(dto.getGroupKey(), Boolean.TRUE);
        return archivedPageInfo;
    }

    String findBlogPostPage(final ArchivedPageInfo archivedPageInfo) {
        ResponseEntity<String> blogPostResponse = webArchiveApiCaller.findBlogPostPage(
            archivedPageInfo);
        return blogPostResponse.getBody();
    }

    String buildPageLinks(final String blogPost) {
        return webPageParser.parse(blogPost);
    }
}
