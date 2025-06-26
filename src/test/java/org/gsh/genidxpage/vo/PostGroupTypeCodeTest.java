package org.gsh.genidxpage.vo;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

class PostGroupTypeCodeTest {

    @DisplayName("주어진 값의 형식에 연관된 그룹 타입을 찾아낼 수 있다")
    @Test
    public void find_group_type_matching_given_value() {
        String yearMonthGroupKey = "2021/12";
        Assertions.assertThat(PostGroupTypeCode.findByGroupKey(yearMonthGroupKey))
            .isEqualTo(PostGroupTypeCode.YEAR_MONTH);

        List.of(
            "Concept %26amp%3B Principle", "Domain-Driven Design", "Software Design"
        ).forEach(categoryGroupKey -> {
                Assertions.assertThat(PostGroupTypeCode.findByGroupKey(categoryGroupKey))
                    .isEqualTo(PostGroupTypeCode.CATEGORY);
            }
        );
    }
}
