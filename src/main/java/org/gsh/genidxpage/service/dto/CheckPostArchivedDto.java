package org.gsh.genidxpage.service.dto;

public class CheckPostArchivedDto {

    private final String url;
    private final String timestamp;

    public CheckPostArchivedDto(String year, String month) {
        this.url = "agile.egloos.com/archives/" + year + "/" + month;
        this.timestamp = "20240101";
    }

    public String getUrl() {
        return url;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
