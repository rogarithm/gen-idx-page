package org.gsh.genidxpage.service;

import org.gsh.genidxpage.dao.PostListPageMapper;
import org.gsh.genidxpage.entity.PostListPage;
import org.springframework.stereotype.Repository;

@Repository
public class PostListPageRecorder {

    private final PostListPageMapper mapper;

    public PostListPageRecorder(PostListPageMapper mapper) {
        this.mapper = mapper;
    }

    void record(PostListPage postListPage) {
        PostListPage hasPostListPage = mapper.selectPostListPageByYearMonth(
            postListPage.getYear(),
            postListPage.getMonth()
        );

        if (hasPostListPage != null) {
            mapper.updatePostListPage(postListPage);
            return;
        }

        mapper.insertPostListPage(postListPage);
    }
}
