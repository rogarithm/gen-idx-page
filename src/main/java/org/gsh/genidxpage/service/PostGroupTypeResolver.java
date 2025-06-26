package org.gsh.genidxpage.service;

import org.gsh.genidxpage.dao.PostGroupTypeMapper;
import org.gsh.genidxpage.entity.PostGroupType;
import org.gsh.genidxpage.vo.PostGroupTypeCode;
import org.springframework.stereotype.Component;

@Component
public class PostGroupTypeResolver {

    private final PostGroupTypeMapper mapper;

    public PostGroupTypeResolver(PostGroupTypeMapper mapper) {
        this.mapper = mapper;
    }

    public PostGroupType resolve(String groupKey) {
        PostGroupTypeCode code = PostGroupTypeCode.findByGroupKey(groupKey);
        return mapper.selectByGroupType(code.getGroupType());
    }
}
