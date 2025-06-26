package org.gsh.genidxpage.vo;

import java.util.Arrays;

public enum PostGroupTypeCode {
    YEAR_MONTH("year_month", "[0-9]{4}/[0-9]{2}"),
    CATEGORY("category", "^.+$");

    private final String groupType;
    private final String pattern;

    PostGroupTypeCode(String groupType, String pattern) {
        this.groupType = groupType;
        this.pattern = pattern;
    }

    public static PostGroupTypeCode findByGroupKey(String groupKey) {
        return Arrays.stream(PostGroupTypeCode.values())
            .filter(groupType -> groupKey.matches(groupType.pattern))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Invalid group key: " + groupKey));
    }

    public String getGroupType() {
        return groupType;
    }
}
