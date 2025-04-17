package org.gsh.genidxpage.service;

import org.assertj.core.api.Assertions;
import org.gsh.genidxpage.service.WebPageParser.PostLinkInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WebPageParserTest {

    @DisplayName("문자열로 된 블로그 글 목록 html 페이지로부터 태그 규칙에 맞는 html 요소를 검색할 수 있다")
    @Test
    public void find_matching_html_elem_from_post_list_html_page_string() throws IOException {
        Path path = Paths.get("src/test/resources/2021-03-full-response.html");
        String fileContent = Files.readString(path, StandardCharsets.UTF_8);
        WebPageParser webPageParser = new WebPageParser();
        PostLinkInfo postLink = webPageParser.findPostLinks(fileContent).get(0);

        Assertions.assertThat(postLink.getPageTitle())
            .isEqualTo("올해 첫 AC2 과정 40기가 곧 열립니다");
        Assertions.assertThat(postLink.getPageUrl())
            .isEqualTo("/web/20230614220926/http://agile.egloos.com/5946833");
    }
}
