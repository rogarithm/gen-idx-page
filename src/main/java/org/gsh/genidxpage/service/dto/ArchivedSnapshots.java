package org.gsh.genidxpage.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

class ArchivedSnapshots {

    @JsonProperty("closest")
    private ClosestSnapshot snapshot;

    ArchivedSnapshots() {
    }

    ArchivedSnapshots(ClosestSnapshot snapshot) {
        this.snapshot = snapshot;
    }

    public ClosestSnapshot getSnapshot() {
        return snapshot;
    }
}
