package org.gsh.genidxpage.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.gsh.genidxpage.dao.PostListPageMapper;
import org.gsh.genidxpage.entity.PostListPage;
import org.gsh.genidxpage.service.dto.ArchivedPageInfo;
import org.gsh.genidxpage.service.dto.ArchivedPageInfoBuilder;
import org.gsh.genidxpage.service.dto.CheckPostArchivedDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class PostListPageRecorderTest {

    @DisplayName("블로그 목록 페이지의 url을 db에 기록한다")
    @Test
    public void record_post_list_page_url_to_db() {
        PostListPageMapper mapper = mock(PostListPageMapper.class);
        PostListPageRecorder recorder = new PostListPageRecorder(mapper);

        CheckPostArchivedDto dto = new CheckPostArchivedDto("2021", "3");
        ArchivedPageInfo archivedPageInfo = ArchivedPageInfoBuilder.builder()
            .withAccessibleArchivedSnapshots()
            .build();
        recorder.record(dto, archivedPageInfo);

        verify(mapper).insert(any(PostListPage.class));
    }

    @DisplayName("이미 등록된 연월의 url이면 업데이트만 한다")
    @Test
    public void only_update_when_already_inserted_url_of_year_month() {
        PostListPageMapper mapper = mock(PostListPageMapper.class);
        PostListPageRecorder recorder = new PostListPageRecorder(mapper);

        PostListPage postListPage = new PostListPage(
            "2021",
            "3",
            "http://localhost:8080/web/20230614220926/archives/2021/03",
            LocalDateTime.now()
        );
        when(mapper.selectByYearMonth(any(), any())).thenReturn(postListPage);

        CheckPostArchivedDto dto = new CheckPostArchivedDto("2021", "3");
        ArchivedPageInfo archivedPageInfo = ArchivedPageInfoBuilder.builder()
            .withAccessibleArchivedSnapshots()
            .build();
        recorder.record(dto, archivedPageInfo);

        verify(mapper).update(any(PostListPage.class));
    }
}
