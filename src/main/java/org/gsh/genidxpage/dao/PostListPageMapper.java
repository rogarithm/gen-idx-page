package org.gsh.genidxpage.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.gsh.genidxpage.entity.PostListPage;

@Mapper
public interface PostListPageMapper {

    Long insert(PostListPage postListPage);

    PostListPage selectByYearMonth(@Param("year") String year, @Param("month") String month);

    Long update(PostListPage postListPage);
}
