package org.gsh.genidxpage.service;

import org.gsh.genidxpage.dao.PostGroupTypeMapper;
import org.gsh.genidxpage.entity.PostGroupType;
import org.gsh.genidxpage.exception.InvalidPostGroupTypeException;
import org.gsh.genidxpage.vo.GroupKey;
import org.gsh.genidxpage.vo.PostGroupTypeCode;
import org.springframework.stereotype.Component;

@Component
public class PostGroupTypeResolver {

    private static final String UNSUPPORTED_GROUP_TYPE = "unsupported";
    private final PostGroupTypeMapper mapper;

    public PostGroupTypeResolver(PostGroupTypeMapper mapper) {
        this.mapper = mapper;
    }

    public PostGroupType resolve(GroupKey groupKey) {
        try {
            PostGroupTypeCode code = PostGroupTypeCode.findByGroupKey(groupKey);
            return mapper.selectByGroupType(code.getGroupType());
        } catch (InvalidPostGroupTypeException e) {
            return mapper.selectByGroupType(UNSUPPORTED_GROUP_TYPE);
        }
    }
}
