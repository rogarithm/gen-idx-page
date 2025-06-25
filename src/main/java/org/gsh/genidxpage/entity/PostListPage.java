package org.gsh.genidxpage.entity;

import org.gsh.genidxpage.service.dto.ArchivedPageInfo;
import org.gsh.genidxpage.service.dto.CheckPostArchivedDto;

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

    PostListPage(String groupKey, String url, LocalDateTime createdAt,
        LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.groupKey = groupKey;
        this.url = url;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public static PostListPage createFrom(CheckPostArchivedDto dto, ArchivedPageInfo archivedPageInfo) {
        return new PostListPage(
            dto.getGroupKey(),
            archivedPageInfo.accessibleUrl(),
            LocalDateTime.now(),
            null,
            null
        );
    }

    public static PostListPage updateFrom(PostListPage postListPage, ArchivedPageInfo archivedPageInfo) {
        return new PostListPage(
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
