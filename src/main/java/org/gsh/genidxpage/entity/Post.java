package org.gsh.genidxpage.entity;

import java.time.LocalDateTime;

public class Post {

    private Long id;
    private Long parentPageId;
    private String rawHtml;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public Post() {}

    public Post(Long parentPageId, String rawHtml, LocalDateTime createdAt) {
        this.parentPageId = parentPageId;
        this.rawHtml = rawHtml;
        this.createdAt = createdAt;
    }

    public static Post of(String rawHtml, Long listPageId) {
        return new Post(
            listPageId,
            rawHtml,
            LocalDateTime.now()
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
