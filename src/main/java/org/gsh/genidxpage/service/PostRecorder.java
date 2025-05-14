package org.gsh.genidxpage.service;

import org.gsh.genidxpage.dao.PostMapper;
import org.gsh.genidxpage.entity.Post;
import org.springframework.stereotype.Repository;

@Repository
public class PostRecorder {

    private final PostMapper mapper;

    public PostRecorder(PostMapper mapper) {
        this.mapper = mapper;
    }

    public void record(String postLinkInfoList, Long listPageId) {
        Post post = Post.of(postLinkInfoList, listPageId);
        mapper.insertPost(post);
    }
}
