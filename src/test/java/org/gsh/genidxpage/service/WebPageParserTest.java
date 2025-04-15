package org.gsh.genidxpage.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.assertj.core.api.Assertions;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class WebPageParserTest {
	@DisplayName("파일로 된 블로그 글 목록 html 페이지로부터 태그 규칙에 맞는 html 요소를 검색할 수 있다")
	@Test
	public void find_matching_html_elems_from_post_list_html_page_file() throws IOException {
		File input = new File("src/test/resources/2021-03-full-response.html");
		Document doc = Jsoup.parse(input, "UTF-8", "http://example.com/");
		WebPageParser webPageParser = new WebPageParser();
		Elements resultDivs = webPageParser.findPostLinks(doc);

		Assertions.assertThat(resultDivs.get(0).text())
			.isEqualTo("올해 첫 AC2 과정 40기가 곧 열립니다");
		Assertions.assertThat(resultDivs.get(0).attribute("href").getValue())
			.isEqualTo("/web/20230614220926/http://agile.egloos.com/5946833");
	}

	@DisplayName("문자열로 된 블로그 글 목록 html 페이지로부터 태그 규칙에 맞는 html 요소를 검색할 수 있다")
	@Test
	public void find_matching_html_elems_from_post_list_html_page_string() throws IOException {
		Path path = Paths.get("src/test/resources/2021-03-full-response.html");
		String fileContent = Files.readString(path, StandardCharsets.UTF_8);
		Document doc = Jsoup.parse(fileContent);
		WebPageParser webPageParser = new WebPageParser();
		Elements resultDivs = webPageParser.findPostLinks(doc);

		Assertions.assertThat(resultDivs.get(0).text())
			.isEqualTo("올해 첫 AC2 과정 40기가 곧 열립니다");
		Assertions.assertThat(resultDivs.get(0).attribute("href").getValue())
			.isEqualTo("/web/20230614220926/http://agile.egloos.com/5946833");
	}
}
