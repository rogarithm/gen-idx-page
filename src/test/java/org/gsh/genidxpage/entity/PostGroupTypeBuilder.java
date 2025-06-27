package org.gsh.genidxpage.entity;

import java.time.LocalDateTime;

public class PostGroupTypeBuilder {

    private Long id;
    private String groupType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static PostGroupTypeBuilder builder() {
        return new PostGroupTypeBuilder();
    }

    public PostGroupTypeBuilder withYearMonthGroupType() {
        this.groupType = "year_month";
        return this;
    }

    public PostGroupType buildAsNew() {
        return new PostGroupType(
            1L,
            this.groupType,
            LocalDateTime.now(),
            null,
            null
        );
    }
}
