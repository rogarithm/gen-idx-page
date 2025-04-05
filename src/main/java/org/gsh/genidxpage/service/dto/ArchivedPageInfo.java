package org.gsh.genidxpage.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ArchivedPageInfo {

    private String url;
    @JsonProperty("archived_snapshots")
    private Object archivedSnapshots;
    @JsonProperty("timestamp")
    private String timestamp;

    public ArchivedPageInfo() {
    }

    public ArchivedPageInfo(String url, Object archivedSnapshots, String timestamp) {
        this.url = url;
        this.archivedSnapshots = archivedSnapshots;
        this.timestamp = timestamp;
    }

    public String getUrl() {
        return url;
    }
}
