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

    public ArchiveStatus(String year, String month, Boolean pageExists, LocalDateTime createdAt) {
        this.year = year;
        this.month = month;
        this.pageExists = pageExists;
        this.createdAt = createdAt;
    }

    public static ArchiveStatus from(CheckPostArchivedDto dto, Boolean pageExists) {
        return new ArchiveStatus(
            dto.getYear(),
            dto.getMonth(),
            pageExists,
            LocalDateTime.now()
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
