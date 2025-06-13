package org.gsh.genidxpage.repository;

import org.gsh.genidxpage.dao.PostListPageMapper;
import org.gsh.genidxpage.entity.PostListPage;
import org.gsh.genidxpage.service.dto.ArchivedPageInfo;
import org.gsh.genidxpage.service.dto.CheckPostArchivedDto;
import org.springframework.stereotype.Repository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class PostListPageRecorder {

    private final PostListPageMapper mapper;

    public PostListPageRecorder(PostListPageMapper mapper) {
        this.mapper = mapper;
    }

    public Long record(final CheckPostArchivedDto dto, final ArchivedPageInfo archivedPageInfo) {
        PostListPage hasPostListPage = mapper.selectByYearMonth(
            dto.getYear(),
            dto.getMonth()
        );

        if (hasPostListPage != null) {
            log.info(
                "updating access url with id of " + hasPostListPage.getId() + " with content of "
                    + archivedPageInfo.accessibleUrl());
            mapper.update(PostListPage.updateFrom(hasPostListPage, archivedPageInfo));
            return hasPostListPage.getId();
        }

        log.info("inserting access url of " + archivedPageInfo.accessibleUrl());
        PostListPage postListPage = PostListPage.createFrom(dto, archivedPageInfo);
        mapper.insert(postListPage);
        return postListPage.getId();
    }
}
