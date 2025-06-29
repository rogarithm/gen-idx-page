package org.gsh.genidxpage.entity;

import java.time.LocalDateTime;

public class ArchiveStatus {

    private Long id;
    private Long postGroupTypeId;
    private String groupKey;
    private Boolean pageExists;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    private ArchiveStatus() {}

    ArchiveStatus(Long postGroupTypeId, String groupKey, Boolean pageExists, LocalDateTime createdAt,
        LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.postGroupTypeId = postGroupTypeId;
        this.groupKey = groupKey;
        this.pageExists = pageExists;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public static ArchiveStatus createFrom(Long postGroupTypeId, String groupKey,
        Boolean pageExists) {
        return new ArchiveStatus(
            postGroupTypeId,
            groupKey,
            pageExists,
            LocalDateTime.now(),
            null,
            null
        );
    }

    public static ArchiveStatus updateFrom(Long postGroupTypeId, ArchiveStatus archiveStatus,
        Boolean pageExists) {
        return new ArchiveStatus(
            postGroupTypeId,
            archiveStatus.getGroupKey(),
            pageExists,
            null,
            LocalDateTime.now(),
            null
        );
    }

    public String getGroupKey() {
        return groupKey;
    }

    public Boolean getPageExists() {
        return pageExists;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
