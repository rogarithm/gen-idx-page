package org.gsh.genidxpage.dao;

import org.apache.ibatis.annotations.Mapper;
import org.gsh.genidxpage.entity.Post;

@Mapper
public interface PostMapper {

    Long insertPost(Post post);
}
