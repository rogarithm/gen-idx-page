package org.gsh.genidxpage.dao;

import org.apache.ibatis.annotations.Mapper;
import org.gsh.genidxpage.entity.Post;

import java.util.List;

@Mapper
public interface PostMapper {

    Long insertPost(Post post);

    Post selectByParentPageId(Long parentPageId);

    Long updatePost(Post post);

    List<Post> selectAll();
}
