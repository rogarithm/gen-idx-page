package org.gsh.genidxpage.web;

import org.gsh.genidxpage.service.ArchivePageService;
import org.gsh.genidxpage.service.WebArchiveApiCaller;
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
        ArchivePageService service = new ArchivePageService(webArchiveApiCaller);
        ArchivedPageInfo archivedPageInfo = service.findArchivedPageInfo(dto);

        String blogPost = service.findBlogPostPage(archivedPageInfo);

        String pageLinks = service.buildPageLinks(blogPost);

        return ResponseEntity.status(HttpStatus.OK)
            .body(pageLinks);
    }
}
