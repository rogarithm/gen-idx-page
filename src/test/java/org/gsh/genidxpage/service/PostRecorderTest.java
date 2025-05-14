package org.gsh.genidxpage.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.gsh.genidxpage.dao.PostMapper;
import org.gsh.genidxpage.entity.Post;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PostRecorderTest {

    @DisplayName("db에 블로그 링크 목록을 나타내는 html을 기록한다")
    @Test
    public void write_raw_html_of_link_list_to_db() {
        PostMapper mapper = mock(PostMapper.class);
        PostRecorder recorder = new PostRecorder(mapper);

        recorder.record("", 0L);

        verify(mapper).insertPost(any(Post.class));
    }
}
