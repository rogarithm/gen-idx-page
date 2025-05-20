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

    public PostListPage(Long id, String year, String month, String url, LocalDateTime createdAt) {
        this.id = id;
        this.year = year;
        this.month = month;
        this.url = url;
        this.createdAt = createdAt;
    }

    public PostListPage(String year, String month, String url, LocalDateTime createdAt) {
        this.year = year;
        this.month = month;
        this.url = url;
        this.createdAt = createdAt;
    }

    PostListPage(String year, String month, String url, LocalDateTime createdAt,
        LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.year = year;
        this.month = month;
        this.url = url;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public static PostListPage createFrom(CheckPostArchivedDto dto, ArchivedPageInfo archivedPageInfo) {
        return new PostListPage(
            dto.getYear(),
            dto.getMonth(),
            archivedPageInfo.accessibleUrl(),
            LocalDateTime.now()
        );
    }

    public static PostListPage updateFrom(PostListPage postListPage, ArchivedPageInfo archivedPageInfo) {
        return new PostListPage(
            postListPage.getYear(),
            postListPage.getMonth(),
            archivedPageInfo.accessibleUrl(),
            null,
            LocalDateTime.now(),
            null
        );
    }

    public Long getId() {
        return id;
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
