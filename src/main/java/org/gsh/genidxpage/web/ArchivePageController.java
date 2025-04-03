package org.gsh.genidxpage.web;

import org.gsh.genidxpage.service.WebArchiveApiCaller;
import org.gsh.genidxpage.service.dto.FindBlogPostDto;
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

    @GetMapping("/posts/{year}/{month}")
    public ResponseEntity<String> getBlogPost(@PathVariable(value = "year") String year,
        @PathVariable(value = "month") String month) {
        FindBlogPostDto dto = new FindBlogPostDto(year, month);
        ResponseEntity<String> response = webArchiveApiCaller.findBlogPost("/posts/{year}/{month}",
            dto);
        return ResponseEntity.status(response.getStatusCode())
            .body(response.getBody());
    }
}
