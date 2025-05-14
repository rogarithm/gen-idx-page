package org.gsh.genidxpage.entity;

import org.gsh.genidxpage.service.dto.ArchivedPageInfo;
import org.gsh.genidxpage.service.dto.CheckPostArchivedDto;

import java.time.LocalDateTime;

public class PostListPage {

    private Long id;
    private String year;
    private String month;
    private String url;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public PostListPage() {}

    public PostListPage(String year, String month, String url, LocalDateTime createdAt) {
        this.year = year;
        this.month = month;
        this.url = url;
        this.createdAt = createdAt;
    }

    public static PostListPage of(CheckPostArchivedDto dto, ArchivedPageInfo archivedPageInfo) {
        return new PostListPage(
            dto.getYear(),
            dto.getMonth(),
            archivedPageInfo.accessibleUrl(),
            LocalDateTime.now()
        );
    }

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public String getUrl() {
        return url;
    }
}
