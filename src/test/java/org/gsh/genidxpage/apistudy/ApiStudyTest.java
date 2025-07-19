package org.gsh.genidxpage.apistudy;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ApiStudyTest {

    @DisplayName("정규식 패턴을 기준으로 그룹핑할 수 있다")
    @Test
    public void group_by_pattern() {
        String regex = "[12][0-9]{3}/[01][0-9]";
        List<String> list = List.of("2021/03", "2020/05", "other");
        Map<Boolean, List<String>> groups = list.stream()
            .collect(Collectors.groupingBy(groupKey -> Pattern.matches(regex, groupKey)));
        Assertions.assertThat(groups.get(true)).hasSameElementsAs(List.of("2021/03", "2020/05"));
        Assertions.assertThat(groups.get(false)).hasSameElementsAs(List.of("other"));
    }
}
