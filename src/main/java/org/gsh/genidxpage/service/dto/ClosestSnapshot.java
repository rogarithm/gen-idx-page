package org.gsh.genidxpage.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

class ClosestSnapshot {

    private String status;
    private Boolean available;
    @JsonProperty("url")
    private String url;
    @JsonProperty("timestamp")
    private String timestamp;

    ClosestSnapshot() {
    }

    ClosestSnapshot(String status, Boolean available, String url, String timestamp) {
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
