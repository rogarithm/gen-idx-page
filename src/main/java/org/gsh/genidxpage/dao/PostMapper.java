package org.gsh.genidxpage.dao;

import org.apache.ibatis.annotations.Mapper;
import org.gsh.genidxpage.entity.Post;

@Mapper
public interface PostMapper {

    Long insert(Post post);

    Post selectByParentPageId(Long parentPageId);

    Long update(Post post);
}
