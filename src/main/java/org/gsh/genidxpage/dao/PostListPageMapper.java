package org.gsh.genidxpage.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.gsh.genidxpage.entity.PostListPage;

@Mapper
public interface PostListPageMapper {

    Long insert(PostListPage postListPage);

    PostListPage selectByGroupKey(@Param("groupKey") String groupKey);

    Long update(PostListPage postListPage);
}
