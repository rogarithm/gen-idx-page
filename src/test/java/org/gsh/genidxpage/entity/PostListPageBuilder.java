package org.gsh.genidxpage.entity;

import java.time.LocalDateTime;

public class PostListPageBuilder {

    private Long id;
    private String groupKey;
    private String url;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static PostListPageBuilder builder() {
        return new PostListPageBuilder();
    }

    public PostListPageBuilder withGroupKey(String groupKey) {
        this.groupKey = groupKey;
        return this;
    }

    public PostListPageBuilder withUrl(String url) {
        this.url = url;
        return this;
    }

    public PostListPage buildAsNew() {
        return new PostListPage(
            this.groupKey,
            this.url,
            LocalDateTime.now(),
            null,
            null
        );
    }
}
