package org.gsh.genidxpage.repository;

import org.gsh.genidxpage.dao.PostListPageMapper;
import org.gsh.genidxpage.entity.PostGroupType;
import org.gsh.genidxpage.entity.PostListPage;
import org.gsh.genidxpage.service.PostGroupTypeResolver;
import org.gsh.genidxpage.service.dto.ArchivedPageInfo;
import org.springframework.stereotype.Repository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class PostListPageRecorder {

    private final PostListPageMapper mapper;
    private final PostGroupTypeResolver resolver;

    public PostListPageRecorder(PostListPageMapper mapper, PostGroupTypeResolver resolver) {
        this.mapper = mapper;
        this.resolver = resolver;
    }

    public Long record(final String groupKey, final ArchivedPageInfo archivedPageInfo) {
        PostGroupType postGroupType = resolver.resolve(groupKey);
        PostListPage hasPostListPage = mapper.selectByGroupKey(groupKey);

        if (hasPostListPage != null) {
            log.debug(
                "updating access url with id of " + hasPostListPage.getId() + " with content of "
                    + archivedPageInfo.accessibleUrl());
            mapper.update(PostListPage.updateFrom(postGroupType.getId(), hasPostListPage, archivedPageInfo));
            return hasPostListPage.getId();
        }

        log.debug("inserting access url of " + archivedPageInfo.accessibleUrl());
        PostListPage postListPage = PostListPage.createFrom(postGroupType.getId(), groupKey, archivedPageInfo);
        mapper.insert(postListPage);
        return postListPage.getId();
    }
}
