package org.gsh.genidxpage.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ArchivedPageInfo {

    private String url;
    @JsonProperty("archived_snapshots")
    private ArchivedSnapshots archivedSnapshots;
    @JsonProperty("timestamp")
    private String timestamp;

    public ArchivedPageInfo() {
    }

    public ArchivedPageInfo(String url, ArchivedSnapshots archivedSnapshots, String timestamp) {
        this.url = url;
        this.archivedSnapshots = archivedSnapshots;
        this.timestamp = timestamp;
    }

    public String getUrl() {
        return url;
    }

    public ArchivedSnapshots getArchivedSnapshots() {
        return archivedSnapshots;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public boolean isAccessible() {
        return archivedSnapshots.getSnapshot() != null;
    }

    public String accessibleUrl() {
        return archivedSnapshots.getSnapshot().getUrl();
    }

    public boolean isEmpty() {
        return false;
    }

    public boolean isUnreachable() {
        return false;
    }
}
