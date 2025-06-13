package org.gsh.genidxpage.repository;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.assertj.core.api.Assertions;
import org.gsh.genidxpage.dao.IndexContentMapper;
import org.gsh.genidxpage.vo.IndexContent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

class IndexContentReaderTest {

    @DisplayName("기록된 모든 연월과 html을 읽어온다")
    @Test
    public void read_all_year_month_and_raw_html() {
        IndexContentMapper mapper = mock(IndexContentMapper.class);
        IndexContentReader reader = new IndexContentReader(mapper);

        List<IndexContent> indexContents = List.of(
            IndexContent.from("2021", "3", "blogUrl1"),
            IndexContent.from("2021", "3", "blogUrl2")
        );
        when(mapper.selectAll()).thenReturn(
            indexContents
        );

        reader.readAllIndexContent().stream().forEach(
            indexContent -> Assertions.assertThat(
                indexContent.matches("\\d{4}/\\d{2}:\\.*")
            )
        );

        verify(mapper).selectAll();
    }
}
