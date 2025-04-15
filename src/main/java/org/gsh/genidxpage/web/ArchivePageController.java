package org.gsh.genidxpage.web;

import org.gsh.genidxpage.service.WebArchiveApiCaller;
import org.gsh.genidxpage.service.WebPageParser;
import org.gsh.genidxpage.service.dto.ArchivedPageInfo;
import org.gsh.genidxpage.service.dto.CheckPostArchivedDto;
import org.jsoup.select.Elements;
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

    @GetMapping("/post-lists/{year}/{month}")
    public ResponseEntity<String> getBlogPostListPage(
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
        Elements postLinks = webPageParser.findPostLinks(blogPost);

        String baseUrl = "https://web.archive.org";
        String pageUrl = postLinks.get(0).attribute("href").getValue();
        String pageTitle = postLinks.get(0).text();
        String pageLink = String.format("<a href=\"%s%s\">%s</a>", baseUrl, pageUrl, pageTitle);

        return ResponseEntity.status(blogPostResponse.getStatusCode())
            .body(pageLink);
    }
}
