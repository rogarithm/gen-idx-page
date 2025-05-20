package org.gsh.genidxpage.entity;

import org.gsh.genidxpage.service.dto.CheckPostArchivedDto;

import java.time.LocalDateTime;

public class ArchiveStatus {

    private Long id;
    private String year;
    private String month;
    private Boolean pageExists;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public ArchiveStatus() {}

    public ArchiveStatus(String year, String month, Boolean pageExists) {
        this.year = year;
        this.month = month;
        this.pageExists = pageExists;
    }

    ArchiveStatus(String year, String month, Boolean pageExists, LocalDateTime createdAt,
        LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.year = year;
        this.month = month;
        this.pageExists = pageExists;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public static ArchiveStatus createFrom(CheckPostArchivedDto dto, Boolean pageExists) {
        return new ArchiveStatus(
            dto.getYear(),
            dto.getMonth(),
            pageExists,
            LocalDateTime.now(),
            null,
            null
        );
    }

    public static ArchiveStatus updateFrom(ArchiveStatus archiveStatus, Boolean pageExists) {
        return new ArchiveStatus(
            archiveStatus.getYear(),
            archiveStatus.getMonth(),
            pageExists,
            null,
            LocalDateTime.now(),
            null
        );
    }

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public Boolean getPageExists() {
        return pageExists;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
