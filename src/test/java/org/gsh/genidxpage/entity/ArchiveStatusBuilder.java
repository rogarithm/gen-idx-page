package org.gsh.genidxpage.entity;

import java.time.LocalDateTime;

public class ArchiveStatusBuilder {

    private Long id;
    private String groupKey;
    private Boolean pageExists;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static ArchiveStatusBuilder builder() {
        return new ArchiveStatusBuilder();
    }

    public ArchiveStatusBuilder withGroupKey(String groupKey) {
        this.groupKey = groupKey;
        return this;
    }

    public ArchiveStatusBuilder thatExists() {
        this.pageExists = Boolean.TRUE;
        return this;
    }

    public ArchiveStatusBuilder thatNotExists() {
        this.pageExists = Boolean.FALSE;
        return this;
    }

    public ArchiveStatus buildAsNew() {
        return new ArchiveStatus(
            this.groupKey,
            this.pageExists,
            LocalDateTime.now(),
            null,
            null
        );
    }
}
