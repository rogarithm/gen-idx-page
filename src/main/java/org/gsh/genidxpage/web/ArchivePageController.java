package org.gsh.genidxpage.web;

import org.gsh.genidxpage.service.WebArchiveApiCaller;
import org.gsh.genidxpage.service.WebPageParser;
import org.gsh.genidxpage.service.WebPageParser.PostLinkInfo;
import org.gsh.genidxpage.service.dto.ArchivedPageInfo;
import org.gsh.genidxpage.service.dto.CheckPostArchivedDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
@Controller
public class ArchivePageController {

    private final WebArchiveApiCaller webArchiveApiCaller;

    public ArchivePageController(final WebArchiveApiCaller webArchiveApiCaller) {
        this.webArchiveApiCaller = webArchiveApiCaller;
    }

    @GetMapping("/post-links/{year}/{month}")
    public ResponseEntity<String> getBlogPostLinks(
        @PathVariable(value = "year") String year,
        @PathVariable(value = "month") String month
    ) {
        CheckPostArchivedDto dto = new CheckPostArchivedDto(year, month);
        ArchivedPageInfo archivedPageInfo = webArchiveApiCaller.findArchivedPageInfo(dto);

        if (!webArchiveApiCaller.isArchived(archivedPageInfo)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("resource not found");
        }

        ResponseEntity<String> blogPostResponse = webArchiveApiCaller.findBlogPostPage(
            archivedPageInfo);
        String blogPost = blogPostResponse.getBody();

        WebPageParser webPageParser = new WebPageParser();
        PostLinkInfo postLinks = webPageParser.findPostLinks(blogPost);
        String pageLink = postLinks.buildPageLink();

        return ResponseEntity.status(blogPostResponse.getStatusCode())
            .body(pageLink);
    }
}
