package org.gsh.genidxpage.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.gsh.genidxpage.entity.PostListPage;

@Mapper
public interface PostListPageMapper {

    Long insertPostListPage(PostListPage postListPage);

    PostListPage selectPostListPageByYearMonth(@Param("year") String year, @Param("month") String month);

    Long updatePostListPage(PostListPage postListPage);
}
