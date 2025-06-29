package org.gsh.genidxpage.dao;

import org.apache.ibatis.annotations.Mapper;
import org.gsh.genidxpage.entity.PostGroupType;

@Mapper
public interface PostGroupTypeMapper {

    PostGroupType selectByGroupType(String groupType);
}
