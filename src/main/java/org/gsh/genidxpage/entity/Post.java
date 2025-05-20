package org.gsh.genidxpage.entity;

import java.time.LocalDateTime;

public class Post {

    private Long id;
    private Long parentPageId;
    private String rawHtml;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    private Post() {}

    Post(Long parentPageId, String rawHtml, LocalDateTime createdAt, LocalDateTime updatedAt,
        LocalDateTime deletedAt) {
        this.parentPageId = parentPageId;
        this.rawHtml = rawHtml;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public static Post createFrom(String rawHtml, Long listPageId) {
        return new Post(
            listPageId,
            rawHtml,
            LocalDateTime.now(),
            null,
            null
        );
    }

    public static Post updateFrom(Post post, String rawHtml) {
        return new Post(
            post.getParentPageId(),
            rawHtml,
            null,
            LocalDateTime.now(),
            null
        );
    }

    public Long getParentPageId() {
        return parentPageId;
    }

    public String getRawHtml() {
        return rawHtml;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
