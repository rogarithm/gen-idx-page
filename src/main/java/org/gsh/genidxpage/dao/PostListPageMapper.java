package org.gsh.genidxpage.dao;

import org.apache.ibatis.annotations.Mapper;
import org.gsh.genidxpage.entity.PostListPage;

@Mapper
public interface PostListPageMapper {

    Long insertPostListPage(PostListPage postListPage);
}
