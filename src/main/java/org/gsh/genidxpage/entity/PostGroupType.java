package org.gsh.genidxpage.entity;

import java.time.LocalDateTime;

public class PostGroupType {

    private Long id;
    private String groupType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public PostGroupType(Long id, String groupType, LocalDateTime createdAt,
        LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.id = id;
        this.groupType = groupType;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public Long getId() {
        return id;
    }

    public String getGroupType() {
        return groupType;
    }
}
