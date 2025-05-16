package org.gsh.genidxpage.service;

import org.gsh.genidxpage.service.dto.ArchivedPageInfo;
import org.gsh.genidxpage.service.dto.CheckPostArchivedDto;
import org.gsh.genidxpage.service.dto.EmptyArchivedPageInfo;
import org.gsh.genidxpage.web.response.PostLinkInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Service
public class AgileStoryArchivePageService implements ArchivePageService {

    private final WebArchiveApiCaller webArchiveApiCaller;
    private final ApiCallReporter reporter;
    private final PostListPageRecorder listPageRecorder;
    private final PostRecorder postRecorder;

    public AgileStoryArchivePageService(WebArchiveApiCaller webArchiveApiCaller,
        ApiCallReporter reporter, PostListPageRecorder listPageRecorder, PostRecorder postRecorder) {
        this.webArchiveApiCaller = webArchiveApiCaller;
        this.reporter = reporter;
        this.listPageRecorder = listPageRecorder;
        this.postRecorder = postRecorder;
    }

    @Transactional
    public String findBlogPageLink(final CheckPostArchivedDto dto) {
        ArchivedPageInfo archivedPageInfo = this.findArchivedPageInfo(dto);
        if (archivedPageInfo.isEmpty()) {
            log.info(
                String.format("empty blog page link for %s/%s", dto.getYear(), dto.getMonth()));
            return "";
        }
        Long listPageId = listPageRecorder.record(dto, archivedPageInfo);
        log.info("id of post list page inserted/updated now is {" + listPageId + "}");

        String blogPost = this.findBlogPostPage(archivedPageInfo);
        postRecorder.record(this.buildPageLinks(blogPost), listPageId);

        return this.buildPageLinks(blogPost);
    }

    @Override
    public List<String> readIndexContent() {
        return postRecorder.readAllRawHtml();
    }

    ArchivedPageInfo findArchivedPageInfo(final CheckPostArchivedDto dto) {
        ArchivedPageInfo archivedPageInfo = webArchiveApiCaller.findArchivedPageInfo(dto);

        if (!webArchiveApiCaller.isArchived(archivedPageInfo)) {
            reporter.reportArchivedPageSearch(dto, Boolean.FALSE);
            return new EmptyArchivedPageInfo();
        }

        reporter.reportArchivedPageSearch(dto, Boolean.TRUE);
        return archivedPageInfo;
    }

    String findBlogPostPage(final ArchivedPageInfo archivedPageInfo) {
        ResponseEntity<String> blogPostResponse = webArchiveApiCaller.findBlogPostPage(
            archivedPageInfo);
        return blogPostResponse.getBody();
    }

    String buildPageLinks(final String blogPost) {
        WebPageParser webPageParser = new WebPageParser();
        List<PostLinkInfo> postLinks = webPageParser.findPostLinks(blogPost);

        String pageLinksConcat = webPageParser.buildPageLinks(postLinks);

        return pageLinksConcat;
    }
}
