package org.gsh.genidxpage.entity;

import java.time.LocalDateTime;

public class PostListPageBuilder {

    private Long id;
    private String year;
    private String month;
    private String url;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static PostListPageBuilder builder() {
        return new PostListPageBuilder();
    }

    public PostListPageBuilder withYearMonth(String year, String month) {
        this.year = year;
        this.month = month;
        return this;
    }

    public PostListPageBuilder withUrl(String url) {
        this.url = url;
        return this;
    }

    public PostListPage buildAsNew() {
        return new PostListPage(
            this.year,
            this.month,
            this.url,
            LocalDateTime.now(),
            null,
            null
        );
    }
}
