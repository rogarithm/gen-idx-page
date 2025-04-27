package org.gsh.genidxpage.service;

import org.assertj.core.api.Assertions;
import org.gsh.genidxpage.web.response.PostLinkInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class WebPageParserTest {

    @DisplayName("문자열로 된 블로그 글 목록 html 페이지로부터 태그 규칙에 맞는 html 요소를 검색할 수 있다")
    @Test
    public void find_matching_html_elem_from_post_list_html_page_string() throws IOException {
        Path path = Paths.get("src/test/resources/2021-03-full-response.html");
        String fileContent = Files.readString(path, StandardCharsets.UTF_8);
        WebPageParser webPageParser = new WebPageParser();
        PostLinkInfo postLink = webPageParser.findPostLinks(fileContent).get(0);

        Assertions.assertThat(postLink.getPageTitle())
            .isNotNull();
        Assertions.assertThat(postLink.getPageUrl())
            .isNotNull();
    }

    @DisplayName("태그 규칙에 맞는 하나 이상의 html 요소를 검색할 수 있다")
    @Test
    public void find_matching_html_elems_from_post_list_html_page_string() throws IOException {
        Path path = Paths.get("src/test/resources/2020-02-response.html");
        String fileContent = Files.readString(path, StandardCharsets.UTF_8);
        WebPageParser webPageParser = new WebPageParser();
        List<PostLinkInfo> postLinks = webPageParser.findPostLinks(fileContent);

        Assertions.assertThat(postLinks.size()).isGreaterThan(1);
        for (PostLinkInfo postLink : postLinks) {
            Assertions.assertThat(postLink.getPageTitle()).isNotNull();
            Assertions.assertThat(postLink.getPageUrl()).isNotNull();
        }
    }

    @DisplayName("클라이언트에 돌려줄 형태로 html 요소 검색 결과를 변환할 수 있다")
    @Test
    public void convert_matching_html_elems_for_client() throws IOException {
        WebPageParser webPageParser = new WebPageParser();

        Path path = Paths.get("src/test/resources/2021-03-full-response.html");
        String fileContent = Files.readString(path, StandardCharsets.UTF_8);
        List<PostLinkInfo> postLinks = webPageParser.findPostLinks(fileContent);
        for (String postLink : webPageParser.buildPageLinks(postLinks).split("\n")) {
            Assertions.assertThat(postLink).matches("<a href=\".*\">.*</a>");
        }

        Path path2 = Paths.get("src/test/resources/2020-02-response.html");
        String fileContent2 = Files.readString(path2, StandardCharsets.UTF_8);
        List<PostLinkInfo> postLinks2 = webPageParser.findPostLinks(fileContent2);
        for (String postLink : webPageParser.buildPageLinks(postLinks2).split("\n")) {
            Assertions.assertThat(postLink).matches("<a href=\".*\">.*</a>");
        }
    }
}
