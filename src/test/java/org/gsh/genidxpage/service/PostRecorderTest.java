package org.gsh.genidxpage.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

        verify(mapper).insert(any(Post.class));
    }

    @DisplayName("db에 부모 페이지 아이디에 연결된 html이 이미 기록되어 있으면, 업데이트한다")
    @Test
    public void only_update_when_raw_html_for_given_parent_page_already_written() {
        PostMapper mapper = mock(PostMapper.class);
        PostRecorder recorder = new PostRecorder(mapper);

        when(mapper.selectByParentPageId(any())).thenReturn(Post.createFrom("", 0L));
        recorder.record("", 0L);

        verify(mapper).update(any(Post.class));
    }
}
