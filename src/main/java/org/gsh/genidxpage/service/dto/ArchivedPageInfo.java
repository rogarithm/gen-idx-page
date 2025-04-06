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

    private static class ArchivedSnapshots {

        @JsonProperty("closest")
        private ClosestSnapshot snapshot;

        public ArchivedSnapshots() {
        }

        public ArchivedSnapshots(ClosestSnapshot snapshot) {
            this.snapshot = snapshot;
        }

        public ClosestSnapshot getSnapshot() {
            return snapshot;
        }
    }

    private static class ClosestSnapshot {

        private String status;
        private Boolean available;
        @JsonProperty("url")
        private String url;
        @JsonProperty("timestamp")
        private String timestamp;

        public ClosestSnapshot() {
        }

        public ClosestSnapshot(String status, Boolean available, String url, String timestamp) {
            this.status = status;
            this.available = available;
            this.url = url;
            this.timestamp = timestamp;
        }

        public String getStatus() {
            return status;
        }

        public Boolean getAvailable() {
            return available;
        }

        public String getUrl() {
            return url;
        }

        public String getTimestamp() {
            return timestamp;
        }
    }
}
