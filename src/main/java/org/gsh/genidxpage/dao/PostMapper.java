package org.gsh.genidxpage.dao;

import org.apache.ibatis.annotations.Mapper;
import org.gsh.genidxpage.entity.Post;

import java.util.List;

@Mapper
public interface PostMapper {

    Long insert(Post post);

    Post selectByParentPageId(Long parentPageId);

    Long update(Post post);

    List<Post> selectAll();
}
