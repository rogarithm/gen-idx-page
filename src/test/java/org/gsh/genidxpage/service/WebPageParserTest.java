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
            .isEqualTo("올해 첫 AC2 과정 40기가 곧 열립니다");
        Assertions.assertThat(postLink.getPageUrl())
            .isEqualTo("/web/20230614220926/http://agile.egloos.com/5946833");
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
        Assertions.assertThat(webPageParser.buildPageLinks(postLinks)).isEqualTo(
            "<a href=\"https://web.archive.org/web/20230614220926/http://agile.egloos.com/5946833\">올해 첫 AC2 과정 40기가 곧 열립니다</a>"
        );

        Path path2 = Paths.get("src/test/resources/2020-02-response.html");
        String fileContent2 = Files.readString(path2, StandardCharsets.UTF_8);
        List<PostLinkInfo> postLinks2 = webPageParser.findPostLinks(fileContent2);
        Assertions.assertThat(webPageParser.buildPageLinks(postLinks2)).isEqualTo(
            "<a href=\"https://web.archive.org/web/20230614124528/http://agile.egloos.com/5932600\">AC2 온라인 과정 : 마인크래프트로 함께 자라기를 배운다</a>"
                + "\n" +
                "<a href=\"https://web.archive.org/web/20230614124528/http://agile.egloos.com/5931859\">혹독한 조언이 나를 살릴까?</a>"
        );
    }
}
