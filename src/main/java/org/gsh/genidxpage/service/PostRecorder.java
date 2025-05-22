package org.gsh.genidxpage.service;

import org.gsh.genidxpage.dao.PostMapper;
import org.gsh.genidxpage.entity.Post;
import org.springframework.stereotype.Repository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class PostRecorder {

    private final PostMapper mapper;

    public PostRecorder(PostMapper mapper) {
        this.mapper = mapper;
    }

    public void record(String rawHtml, Long listPageId) {
        Post hasPost = mapper.selectByParentPageId(listPageId);
        if (hasPost != null) {
            mapper.update(Post.updateFrom(hasPost, rawHtml));
            return;
        }

        mapper.insert(Post.createFrom(rawHtml, listPageId));
    }
}
