package org.gsh.genidxpage.web;

import org.gsh.genidxpage.service.ArchivePageService;
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

    private final ArchivePageService service;

    public ArchivePageController(final ArchivePageService service) {
        this.service = service;
    }

    @GetMapping("/post-links/{year}/{month}")
    public ResponseEntity<String> getBlogPostLinks(
        @PathVariable(value = "year") String year,
        @PathVariable(value = "month") String month
    ) {
        CheckPostArchivedDto dto = new CheckPostArchivedDto(year, month); String pageLinks = service.findBlogPageLink(dto);

        return ResponseEntity.status(HttpStatus.OK)
            .body(pageLinks);
    }
}
