package org.gsh.genidxpage.service.dto;

public class CheckPostArchivedDto implements CheckPostArchived {

    private final String groupKey;
    private final String url;
    private final String timestamp;

    public CheckPostArchivedDto(String groupKey) {
        this.groupKey = groupKey;
        this.url = "https://agile.egloos.com/archives/" + groupKey;
        this.timestamp = "20230401";
    }

    @Override
    public String getGroupKey() {
        return groupKey;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getTimestamp() {
        return timestamp;
    }
}
