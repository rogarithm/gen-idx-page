package org.gsh.genidxpage.vo;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
public enum PostGroupTypeCode {
    YEAR_MONTH("year_month", "[0-9]{4}/[0-9]{2}"),
    CATEGORY("category", "^[\\w %&;#\\-]+$");

    private final String groupType;
    private final String pattern;

    PostGroupTypeCode(String groupType, String pattern) {
        this.groupType = groupType;
        this.pattern = pattern;
    }

    public static PostGroupTypeCode findByGroupKey(String groupKey) {
        PostGroupTypeCode[] postGroupTypeCodes = PostGroupTypeCode.values();
        return Arrays.stream(postGroupTypeCodes)
            .filter(groupType -> groupKey.matches(groupType.pattern))
            .findFirst()
            .orElseThrow(() -> {
                log.warn("Could not find PostGroupTypeCode for groupKey: {}", groupKey);
                return new IllegalArgumentException(
                    "Could not find PostGroupTypeCode for groupKey: " + groupKey
                );
            });
    }

    public String getGroupType() {
        return groupType;
    }
}
