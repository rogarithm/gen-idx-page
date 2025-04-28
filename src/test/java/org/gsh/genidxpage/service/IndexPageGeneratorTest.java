package org.gsh.genidxpage.service;

import org.assertj.core.api.Assertions;
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

        List<String> pageLinksList = List.of("l1\nl2", "l3", "l4");
        IndexPageGenerator generator = new IndexPageGenerator("/tmp/genidxpage");
        generator.generateIndexPage(pageLinksList);
        String fileContent = Files.readString(Path.of("/tmp/genidxpage/index.html"), StandardCharsets.UTF_8);
        Assertions.assertThat(fileContent).contains("l1")
            .contains("l2")
            .contains("l3")
            .contains("l4");
    }
}
