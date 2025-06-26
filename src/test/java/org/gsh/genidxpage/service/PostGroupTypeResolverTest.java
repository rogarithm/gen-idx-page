package org.gsh.genidxpage.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.assertj.core.api.Assertions;
import org.gsh.genidxpage.dao.PostGroupTypeMapper;
import org.gsh.genidxpage.entity.PostGroupType;
import org.gsh.genidxpage.entity.PostGroupTypeBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PostGroupTypeResolverTest {

    @DisplayName("dto로부터 postGroupType 정보를 가져올 수 있다")
    @Test
    public void resolve_group_type_from_dto() {
        PostGroupTypeMapper mapper = mock(PostGroupTypeMapper.class);
        PostGroupTypeResolver resolver = new PostGroupTypeResolver(mapper);

        PostGroupType postGroupType = PostGroupTypeBuilder.builder()
            .withYearMonthGroupType()
            .buildAsNew();
        when(mapper.selectByGroupType(any())).thenReturn(postGroupType);

        PostGroupType expectedPostGroupType = resolver.resolve("2022/03");

        Assertions.assertThat(expectedPostGroupType.getGroupType()).isEqualTo("year_month");
    }
}
