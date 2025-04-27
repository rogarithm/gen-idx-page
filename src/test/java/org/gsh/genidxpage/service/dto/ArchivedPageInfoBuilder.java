package org.gsh.genidxpage.service.dto;

public class ArchivedPageInfoBuilder {
    private String url;
    private ArchivedSnapshots archivedSnapshots;
    private String timestamp;

    public static ArchivedPageInfoBuilder builder() {
        return new ArchivedPageInfoBuilder();
    }

    public ArchivedPageInfoBuilder url(String url) {
        this.url = url;
        return this;
    }

    public ArchivedPageInfoBuilder withEmptyArchivedSnapshots() {
        this.archivedSnapshots = new ArchivedSnapshots();
        return this;
    }

    public ArchivedPageInfoBuilder withAccessibleArchivedSnapshots() {
        this.archivedSnapshots = new ArchivedSnapshots(
            new ClosestSnapshot("200",
                true,
                "http://localhost:8080/web/20230614220926/archives/2021/03",
                "20230614220926")
        );
        return this;
    }

    public ArchivedPageInfoBuilder timestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public ArchivedPageInfo build() {
        return new ArchivedPageInfo(url, archivedSnapshots, timestamp);
    }
}
