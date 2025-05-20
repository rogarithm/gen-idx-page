package org.gsh.genidxpage.service;

import org.gsh.genidxpage.dao.PostMapper;
import org.gsh.genidxpage.entity.Post;
import org.springframework.stereotype.Repository;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

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
            mapper.update(Post.createFrom(rawHtml, listPageId));
            return;
        }

        mapper.insert(Post.createFrom(rawHtml, listPageId));
    }

    public List<String> readAllRawHtml() {
        return mapper.selectAll()
            .stream()
            .map(post -> post.getRawHtml())
            .collect(Collectors.toList());
    }
}
