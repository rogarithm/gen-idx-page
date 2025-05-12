package org.gsh.genidxpage.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.gsh.genidxpage.dao.PostListPageMapper;
import org.gsh.genidxpage.entity.PostListPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class PostListPageRecorderTest {

    @DisplayName("블로그 목록 페이지의 url을 db에 기록한다")
    @Test
    public void record_post_list_page_url_to_db() {
        PostListPageMapper mapper = mock(PostListPageMapper.class);
        PostListPageRecorder recorder = new PostListPageRecorder(mapper);

        PostListPage postListPage = new PostListPage("2021", "3", "url", LocalDateTime.now());

        recorder.record(postListPage);

        verify(mapper).insertPostListPage(any(PostListPage.class));
    }
}
