package org.gsh.genidxpage.entity;

import org.gsh.genidxpage.service.dto.ArchivedPageInfo;
import org.gsh.genidxpage.service.dto.CheckPostArchived;

import java.time.LocalDateTime;

public class PostListPage {

    private Long id;
    private Long postGroupTypeId;
    private String groupKey;
    private String url;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    private PostListPage() {}

    PostListPage(Long postGroupTypeId, String groupKey, String url, LocalDateTime createdAt,
        LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.postGroupTypeId = postGroupTypeId;
        this.groupKey = groupKey;
        this.url = url;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public static PostListPage createFrom(Long postGroupTypeId, CheckPostArchived dto,
        ArchivedPageInfo archivedPageInfo) {
        return new PostListPage(
            postGroupTypeId,
            dto.getGroupKey(),
            archivedPageInfo.accessibleUrl(),
            LocalDateTime.now(),
            null,
            null
        );
    }

    public static PostListPage updateFrom(Long postGroupTypeId, PostListPage postListPage,
        ArchivedPageInfo archivedPageInfo) {
        return new PostListPage(
            postGroupTypeId,
            postListPage.getGroupKey(),
            archivedPageInfo.accessibleUrl(),
            null,
            LocalDateTime.now(),
            null
        );
    }

    public Long getId() {
        return id;
    }

    public String getGroupKey() {
        return groupKey;
    }

    public String getUrl() {
        return url;
    }
}
