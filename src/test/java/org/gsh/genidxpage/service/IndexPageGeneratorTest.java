package org.gsh.genidxpage.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.Assertions;
import org.gsh.genidxpage.repository.IndexContentReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

class IndexPageGeneratorTest {

    @DisplayName("블로그 링크 목록으로 인덱스 페이지를 만들 수 있다")
    @Test
    public void generate_index_page_from_blog_link_list() throws IOException {
        List<String> pageLinksList = List.of(
            "2021/03:<a href=\"https://x.net\">l1</a>",
            "2020/05:<a href=\"https://x.net\">l2</a>",
            "2020/02:<a href=\"https://x.net\">l3</a>\n<a href=\"https://x.net\">l4</a>"
        );
        IndexPageGenerator generator = new AgileStoryIndexPageGenerator("/tmp/genidxpage", null);
        generator.generateIndexPage(pageLinksList);
        String fileContent = Files.readString(Path.of("/tmp/genidxpage/index.html"), StandardCharsets.UTF_8);
        Assertions.assertThat(fileContent).contains("l1")
            .contains("l2")
            .contains("l3")
            .contains("l4");
    }

    @DisplayName("인덱스 페이지에 쓸 내용을 db에서 읽어온다")
    @Test
    public void read_all_index_page_content_from_db() {
        IndexContentReader reader = mock(IndexContentReader.class);
        IndexPageGenerator generator = new AgileStoryIndexPageGenerator(
            "/tmp/genidxpage",
            reader
        );

        generator.readIndexContent();

        verify(reader).readAllIndexContent();
    }
}
