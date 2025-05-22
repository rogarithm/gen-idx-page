package org.gsh.genidxpage.dao;

import org.apache.ibatis.annotations.Mapper;
import org.gsh.genidxpage.vo.IndexContent;

import java.util.List;

@Mapper
public interface IndexContentMapper {

    List<IndexContent> selectAll();
}
