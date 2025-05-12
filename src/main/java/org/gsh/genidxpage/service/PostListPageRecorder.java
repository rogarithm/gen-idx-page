package org.gsh.genidxpage.service;

import org.gsh.genidxpage.dao.PostListPageMapper;
import org.gsh.genidxpage.entity.PostListPage;
import org.gsh.genidxpage.service.dto.ArchivedPageInfo;
import org.gsh.genidxpage.service.dto.CheckPostArchivedDto;
import org.springframework.stereotype.Repository;

@Repository
public class PostListPageRecorder {

    private final PostListPageMapper mapper;

    public PostListPageRecorder(PostListPageMapper mapper) {
        this.mapper = mapper;
    }

    void record(final CheckPostArchivedDto dto, final ArchivedPageInfo archivedPageInfo) {
        PostListPage hasPostListPage = mapper.selectPostListPageByYearMonth(
            dto.getYear(),
            dto.getMonth()
        );

        if (hasPostListPage != null) {
            mapper.updatePostListPage(PostListPage.of(dto, archivedPageInfo));
            return;
        }

        mapper.insertPostListPage(PostListPage.of(dto, archivedPageInfo));
    }
}
